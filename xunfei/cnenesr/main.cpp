
#include <stdlib.h>
#include <stdio.h>
#include <fstream>
#include <assert.h>
#include <cstring>
#include <atomic>
#include <process.h>
#include <conio.h>
#include <errno.h>

// #include <unistd.h>

#include <Windows.h>

#include "include/aikit_biz_api.h"
#include "include/aikit_constant.h"
#include "include/aikit_biz_config.h"
#include "include/aikit_biz_builder.h"

#include "include/speech_recognizer.h"

#define FRAME_LEN 640 // 16k采样率的16bit音频，一帧的大小为640B, 时长20ms

using namespace std;
using namespace AIKIT;

enum
{
	EVT_START = 0,
	EVT_STOP,
	EVT_QUIT,
	EVT_TOTAL
};
static HANDLE events[EVT_TOTAL] = {NULL, NULL, NULL};

static const char *ABILITY = "e75f07b62";

static void show_key_hints(void)
{
	printf("\n\
----------------------------\n\
Press r to start speaking\n\
Press s to end your speaking\n\
Press q to quit\n\
----------------------------\n");
}

void OnOutput(AIKIT_HANDLE *handle, const AIKIT_OutputData *output)
{

	printf("OnOutput abilityID :%s\n", handle->abilityID);
	printf("OnOutput key:%s\n", output->node->key);
	printf("OnOutput value:%s\n", (char *)output->node->value);
}

void OnEvent(AIKIT_HANDLE *handle, AIKIT_EVENT eventType, const AIKIT_OutputEvent *eventValue)
{
	printf("OnEvent:%d\n", eventType);
}

void OnError(AIKIT_HANDLE *handle, int32_t err, const char *desc)
{
	printf("OnError:%d\n", err);
}

/* helper thread: to listen to the keystroke */
static unsigned int __stdcall helper_thread_proc(void *para)
{
	int key;
	int quit = 0;

	do
	{
		key = _getch();
		switch (key)
		{
		case 'r':
		case 'R':
			SetEvent(events[EVT_START]);
			break;
		case 's':
		case 'S':
			SetEvent(events[EVT_STOP]);
			break;
		case 'q':
		case 'Q':
			quit = 1;
			SetEvent(events[EVT_QUIT]);
			PostQuitMessage(0);
			break;
		default:
			break;
		}

		if (quit)
			break;
	} while (1);

	return 0;
}

static HANDLE start_helper_thread()
{
	HANDLE hdl;

	hdl = (HANDLE)_beginthreadex(NULL, 0, helper_thread_proc, NULL, 0, NULL);

	return hdl;
}

static void esr_mic()
{
	int errcode;
	int i = 0;
	HANDLE helper_thread = NULL;

	struct speech_rec esr;
	DWORD waitres;
	char isquit = 0;

	errcode = sr_init(&esr, SR_MIC, DEFAULT_INPUT_DEVID);
	if (errcode)
	{
		printf("speech recognizer init failed\n");
		return;
	}

	for (i = 0; i < EVT_TOTAL; ++i)
	{
		events[i] = CreateEvent(NULL, FALSE, FALSE, NULL);
	}

	helper_thread = start_helper_thread();
	if (helper_thread == NULL)
	{
		printf("create thread failed\n");
		goto exit;
	}

	show_key_hints();

	while (1)
	{
		waitres = WaitForMultipleObjects(EVT_TOTAL, events, FALSE, INFINITE);
		switch (waitres)
		{
		case WAIT_FAILED:
		case WAIT_TIMEOUT:
			printf("Why it happened !?\n");
			break;
		case WAIT_OBJECT_0 + EVT_START:
			if (errcode = sr_start_listening(&esr))
			{
				printf("start listen failed %d\n", errcode);
				isquit = 1;
			}
			break;
		case WAIT_OBJECT_0 + EVT_STOP:
			if (errcode = sr_stop_listening(&esr))
			{
				printf("stop listening failed %d\n", errcode);
				isquit = 1;
			}
			break;
		case WAIT_OBJECT_0 + EVT_QUIT:
			sr_stop_listening(&esr);
			isquit = 1;
			break;
		default:
			break;
		}
		if (isquit)
			break;
	}

exit:
	if (helper_thread != NULL)
	{
		WaitForSingleObject(helper_thread, INFINITE);
		CloseHandle(helper_thread);
	}

	for (i = 0; i < EVT_TOTAL; ++i)
	{
		if (events[i])
			CloseHandle(events[i]);
	}

	sr_uninit(&esr);
}

int esr_file(AIKIT_ParamBuilder *paramBuilder, const char *audio_path, int fsa_count, long *readLen)
{
	int ret = 0;
	FILE *file = nullptr;
	long fileSize = 0;
	long curLen = 0;
	int times = 1;
	char data[320] = {0};
	int *index = (int *)malloc(fsa_count * sizeof(int));

	AIKIT_DataStatus status = AIKIT_DataBegin;
	AIKIT_DataBuilder *dataBuilder = nullptr;
	AIKIT_HANDLE *handle = nullptr;
	AiAudio *aiAudio_raw = nullptr;

	for (int i = 0; i < fsa_count; ++i)
	{
		index[i] = i;
	}
	ret = AIKIT_SpecifyDataSet(ABILITY, "FSA", index, fsa_count);
	if (ret != 0)
	{
		printf("AIKIT_SpecifyDataSet failed:%d\n", ret);
		goto exit;
	}

	ret = AIKIT_Start(ABILITY, AIKIT_Builder::build(paramBuilder), nullptr, &handle);
	if (ret != 0)
	{
		printf("AIKIT_Start failed:%d\n", ret);
		goto exit;
	}

	// file = fopen(".\\testAudio\\cn_test.pcm", "rb");
	file = fopen(audio_path, "rb");
	if (file == nullptr)
	{
		printf("fopen failed\n");
		goto exit;
	}

	// 注意，如果写入音频是wav，请去掉wav的头 44字节
	fseek(file, 0, SEEK_END);
	fileSize = ftell(file);
	fseek(file, *readLen, SEEK_SET);
	// wav文件
	// fseek(file, 44, SEEK_SET);

	dataBuilder = AIKIT_DataBuilder::create();

	while (fileSize > *readLen)
	{
		curLen = fread(data, 1, sizeof(data), file);
		*readLen += curLen;
		dataBuilder->clear();
		if (times == 1)
		{
			status = AIKIT_DataBegin;
			times++;
		}
		else
			status = AIKIT_DataContinue;

		aiAudio_raw = AiAudio::get("audio")->data(data, curLen)->status(status)->valid();
		dataBuilder->payload(aiAudio_raw);

		ret = ESRGetRlt(handle, dataBuilder);
		if (ret != 0)
			goto exit;

		Sleep(10);
	}

	*readLen = -1;
	dataBuilder->clear();
	status = AIKIT_DataEnd;
	aiAudio_raw = AiAudio::get("audio")->data(data, 0)->status(status)->valid();
	dataBuilder->payload(aiAudio_raw);

	ret = ESRGetRlt(handle, dataBuilder);
	if (ret != 0)
		goto exit;

	ret = AIKIT_End(handle);
	if (ret != 0)
	{
		printf("AIKIT_End failed : %d\n", ret);
	}

exit:
	if (handle != nullptr)
		AIKIT_End(handle);

	if (dataBuilder != nullptr)
	{
		delete dataBuilder;
		dataBuilder = nullptr;
	}
	if (file != nullptr)
	{
		fclose(file);
		file = nullptr;
	}
	if (index != NULL)
	{
		free(index);
		index = NULL;
	}

	return ret;
}

void TestCNENEsr()
{
	int ret = 0;
	int aud_src = 0;
	int index[1] = {0};
	int loop = 1;
	long readLen = 0;

	AIKIT_ParamBuilder *paramBuilder = nullptr;
	AIKIT_ParamBuilder *engine_paramBuilder = nullptr;
	AIKIT_CustomBuilder *customBuilder = nullptr;

	engine_paramBuilder = AIKIT_ParamBuilder::create();
	engine_paramBuilder->clear();
	engine_paramBuilder->param("decNetType", "fsa", strlen("fsa"));
	engine_paramBuilder->param("punishCoefficient", 0.0);
	engine_paramBuilder->param("wfst_addType", 0); // 0-中文，1-英文
	ret = AIKIT_EngineInit(ABILITY, AIKIT_Builder::build(engine_paramBuilder));
	if (ret != 0)
	{
		printf("AIKIT_EngineInit failed:%d\n", ret);
		goto exit;
	}

	// 输入个性化数据
	customBuilder = AIKIT_CustomBuilder::create();
	customBuilder->clear();
	//// 插词
	// customBuilder->textPath("FSA", ".\\resource\\cnenesr\\fsa\\en_fsa.txt", 0);
	customBuilder->textPath("FSA", ".\\resource\\cnenesr\\fsa\\cn_fsa.txt", 0);
	ret = AIKIT_LoadData("e75f07b62", AIKIT_Builder::build(customBuilder));

	if (ret != 0)
	{
		printf("AIKIT_LoadData failed:%d\n", ret);
		goto exit;
	}

	printf("===========================\n");
	printf("where is audio data from? \n0: audio file\n1: microphone\n");
	printf("===========================\n");

	scanf("%d", &aud_src);
	if (aud_src)
	{
		esr_mic();
	}
	else
	{
		paramBuilder = AIKIT_ParamBuilder::create();
		paramBuilder->clear();
		paramBuilder->param("languageType", 0); // 0-中文 1-英文
		paramBuilder->param("vadEndGap", 60);
		paramBuilder->param("vadOn", true);
		paramBuilder->param("beamThreshold", 20);
		paramBuilder->param("hisGramThreshold", 3000);
		paramBuilder->param("postprocOn", false);
		paramBuilder->param("vadResponsetime", 1000);
		paramBuilder->param("vadLinkOn", true);
		paramBuilder->param("vadSpeechEnd", 80);

		while (loop--)
		{
			readLen = 0;
			while (readLen >= 0)
			{
				ret = esr_file(paramBuilder, ".\\testAudio\\cn_test.pcm", 1, &readLen);
				if (ret != 0 && ret != ESR_HAS_RESULT)
					goto exit;
			}
		}
	}

exit:

	if (paramBuilder != nullptr)
	{
		delete paramBuilder;
		paramBuilder = nullptr;
	}
	if (customBuilder != nullptr)
	{
		delete customBuilder;
		customBuilder = nullptr;
	}

	if (engine_paramBuilder != nullptr)
	{
		delete engine_paramBuilder;
		engine_paramBuilder = nullptr;
	}
	AIKIT_UnLoadData(ABILITY, "FSA", 0);
	// AIKIT_UnLoadData(ABILITY,"FSA", 1);
}

int main()
{
	AIKIT_Configurator::builder()
		.app()
		.appID("")
		.apiSecret("")
		.apiKey("")
		.workDir(".\\")
		.auth()
		.authType(0)
		.ability(ABILITY)
		.log()
		.logMode(2)
		.logPath(".\\aikit.log");
	int ret = AIKIT_Init();
	if (ret != 0)
	{
		printf("AIKIT_Init failed:%d\n", ret);
		return -1;
	}
	AIKIT_Callbacks cbs = {OnOutput, OnEvent, OnError};
	AIKIT_RegisterAbilityCallback(ABILITY, cbs);

	system("chcp 65001");
	TestCNENEsr();

	system("pause");
	AIKIT_UnInit();
	return 0;
}
