/*
@file
@brief 基于录音接口和MSC接口封装一个MIC录音识别的模块

@author		taozhang9
@date		2016/05/27
*/

#include <stdio.h>
#include <stdlib.h>
#include <windows.h>

#include "./include/winrec.h"
#include "./include/speech_recognizer.h"

using namespace AIKIT;

#define SR_DBGON 0
#if SR_DBGON == 1
#define sr_dbg printf
// #	define __FILE_SAVE_VERIFY__  /* save the recording data into file 'rec.pcm' too */
#else
#define sr_dbg
#endif

#define DEFAULT_FORMAT   \
	{                    \
		WAVE_FORMAT_PCM, \
		1,               \
		16000,           \
		32000,           \
		2,               \
		16,              \
		sizeof(WAVEFORMATEX)}

/* internal state */
enum
{
	SR_STATE_INIT,
	SR_STATE_STARTED
};

/* for debug. saving the recording to a file */
#ifdef __FILE_SAVE_VERIFY__
#define VERIFY_FILE_NAME "rec.pcm"
static int open_stored_file(const char *name);
static int loopwrite_to_file(char *data, size_t length);
static void safe_close_file();
#endif

#define SR_MALLOC malloc
#define SR_MFREE free
#define SR_MEMSET memset

#ifdef __FILE_SAVE_VERIFY__

static FILE *fdwav = NULL;

static int open_stored_file(const char *name)
{
	fdwav = fopen(name, "wb+");
	if (fdwav == NULL)
	{
		printf("error open file failed\n");
		return -1;
	}
	return 0;
}

static int loopwrite_to_file(char *data, size_t length)
{
	size_t wrt = 0, already = 0;
	int ret = 0;
	if (fdwav == NULL || data == NULL)
		return -1;

	while (1)
	{
		wrt = fwrite(data + already, 1, length - already, fdwav);
		if (wrt == (length - already))
			break;
		if (ferror(fdwav))
		{
			ret = -1;
			break;
		}
		already += wrt;
	}

	return ret;
}

static void safe_close_file()
{
	if (fdwav)
	{
		fclose(fdwav);
		fdwav = NULL;
	}
}
#endif

bool is_result = false;
int ESRGetRlt(AIKIT_HANDLE *handle, AIKIT_DataBuilder *dataBuilder)
{
	int ret = 0;
	AIKIT_OutputData *output = nullptr;
	AIKIT_InputData *input_data = AIKIT_Builder::build(dataBuilder);
	ret = AIKIT_Write(handle, input_data);
	if (ret != 0)
	{
		printf("AIKIT_Write:%d\n", ret);
		return ret;
	}
	ret = AIKIT_Read(handle, &output);
	if (ret != 0)
	{
		printf("AIKIT_Read:%d\n", ret);
		return ret;
	}

	if (output != nullptr)
	{
		FILE *fin = fopen("esr_result.txt", "ab");
		if (fin == nullptr)
		{
			printf("文件打开失败!\n");
			return -1;
		}
		AIKIT_BaseData *node = output->node;
		while (node != nullptr && node->value != nullptr)
		{
			fwrite(node->key, sizeof(char), strlen(node->key), fin);
			fwrite(": ", sizeof(char), strlen(": "), fin);
			fwrite(node->value, sizeof(char), node->len, fin);
			fwrite("\n", sizeof(char), strlen("\n"), fin);
			printf("key:%s\tvalue:%s\n", node->key, (char *)node->value);
			if (node->status == 2)
				is_result = true;
			node = node->next;
		}
		fclose(fin);
	}
	if (is_result)
	{
		is_result = false;
		return ESR_HAS_RESULT;
	}

	return ret;
}

static void end_sr(struct speech_rec *sr)
{
	if (sr->aud_src == SR_MIC)
		stop_record(sr->recorder);

	if (sr->handle)
	{
		// printf("end_sr_on_error\n");
		AIKIT_End(sr->handle);
		sr->handle = NULL;
	}
	sr->state = SR_STATE_INIT;
#ifdef __FILE_SAVE_VERIFY__
	safe_close_file();
#endif
}

/* the record call back */
static void esr_cb(char *data, unsigned long len, void *user_para)
{
	int errcode;
	struct speech_rec *sr;

	if (len == 0 || data == NULL)
		return;

	sr = (struct speech_rec *)user_para;

	if (sr == NULL || sr->audio_status >= AIKIT_DataEnd)
		return;

#ifdef __FILE_SAVE_VERIFY__
	loopwrite_to_file(data, len);
#endif

	errcode = sr_write_audio_data(sr, data, len);
	if (errcode)
	{
		end_sr(sr);
		return;
	}
}

/* devid will be ignored if aud_src is not SR_MIC ; if devid == -1, then
 * the default input device will be used.
 */

int sr_init(struct speech_rec *sr, enum sr_audsrc aud_src, int devid)
{
	int errcode;
	int index[] = {0};
	WAVEFORMATEX wavfmt = DEFAULT_FORMAT;

	if (aud_src == SR_MIC && get_input_dev_num() == 0)
	{
		return -E_SR_NOACTIVEDEVICE;
	}

	if (!sr)
		return -E_SR_INVAL;

	SR_MEMSET(sr, 0, sizeof(struct speech_rec));
	sr->state = SR_STATE_INIT;
	sr->aud_src = aud_src;
	sr->audio_status = AIKIT_DataBegin;
	sr->ABILITY = "e75f07b62";
	sr->dataBuilder = nullptr;
	sr->dataBuilder = AIKIT_DataBuilder::create();
	sr->paramBuilder = AIKIT_ParamBuilder::create();

	sr->paramBuilder->clear();
	sr->paramBuilder->param("languageType", 0);
	sr->paramBuilder->param("vadEndGap", 75);
	sr->paramBuilder->param("vadOn", true);
	sr->paramBuilder->param("beamThreshold", 20);
	sr->paramBuilder->param("hisGramThreshold", 3000);
	sr->paramBuilder->param("postprocOn", true);
	sr->paramBuilder->param("vadResponsetime", 1000);
	sr->paramBuilder->param("vadLinkOn", true);
	sr->paramBuilder->param("vadSpeechEnd", 80);

	if (aud_src == SR_MIC)
	{
		errcode = create_recorder(&sr->recorder, esr_cb, (void *)sr);
		if (sr->recorder == NULL || errcode != 0)
		{
			sr_dbg("create recorder failed: %d\n", errcode);
			errcode = -E_SR_RECORDFAIL;
			goto fail;
		}

		// 音频采样率
		wavfmt.nSamplesPerSec = 16000;
		wavfmt.nAvgBytesPerSec = wavfmt.nBlockAlign * wavfmt.nSamplesPerSec;

		errcode = open_recorder(sr->recorder, devid, &wavfmt);
		if (errcode != 0)
		{
			sr_dbg("recorder open failed: %d\n", errcode);
			errcode = -E_SR_RECORDFAIL;
			goto fail;
		}
	}

	return 0;

fail:
	if (sr->recorder)
	{
		destroy_recorder(sr->recorder);
		sr->recorder = NULL;
	}

	return errcode;
}

int sr_start_listening(struct speech_rec *sr)
{
	int ret;
	int errcode = 0;
	int index[] = {0};

	if (sr->state >= SR_STATE_STARTED)
	{
		sr_dbg("already STARTED.\n");
		return -E_SR_ALREADY;
	}
	index[0] = 0;
	errcode = AIKIT_SpecifyDataSet(sr->ABILITY, "FSA", index, sizeof(index) / sizeof(int));
	if (errcode != 0)
		return errcode;
	errcode = AIKIT_Start(sr->ABILITY, AIKIT_Builder::build(sr->paramBuilder), nullptr, &sr->handle);
	if (0 != errcode)
	{
		sr_dbg("\nAIKIT_Start failed! error code:%d\n", errcode);
		return errcode;
	}
	sr->audio_status = AIKIT_DataBegin;

	if (sr->aud_src == SR_MIC)
	{
		ret = start_record(sr->recorder);
		if (ret != 0)
		{
			sr_dbg("start record failed: %d\n", ret);
			ret = AIKIT_End(sr->handle);
			sr->handle = NULL;
			return -E_SR_RECORDFAIL;
		}
#ifdef __FILE_SAVE_VERIFY__
		open_stored_file(VERIFY_FILE_NAME);
#endif
	}

	sr->state = SR_STATE_STARTED;

	printf("Start Listening...\n");

	return 0;
}

/* after stop_record, there are still some data callbacks */
static void wait_for_rec_stop(struct recorder *rec, unsigned int timeout_ms)
{
	while (!is_record_stopped(rec))
	{
		Sleep(1);
		if (timeout_ms != (unsigned int)-1)
			if (0 == timeout_ms--)
				break;
	}
}

int sr_stop_listening(struct speech_rec *sr)
{
	int ret = 0;
	AiAudio *aiAudio_raw = NULL;

	if (sr->state < SR_STATE_STARTED)
	{
		sr_dbg("Not started or already stopped.\n");
		return 0;
	}

	if (sr->aud_src == SR_MIC)
	{
		ret = stop_record(sr->recorder);
#ifdef __FILE_SAVE_VERIFY__
		safe_close_file();
#endif
		if (ret != 0)
		{
			sr_dbg("Stop failed! \n");
			return -E_SR_RECORDFAIL;
		}
		wait_for_rec_stop(sr->recorder, (unsigned int)-1);
	}
	sr->state = SR_STATE_INIT;
	sr->dataBuilder->clear();
	aiAudio_raw = AiAudio::get("audio")->data(NULL, 0)->status(AIKIT_DataEnd)->valid();
	sr->dataBuilder->payload(aiAudio_raw);
	printf("sr_stop_listening\n");
	ret = ESRGetRlt(sr->handle, sr->dataBuilder);
	if (ret != 0 && ret != ESR_HAS_RESULT)
	{
		sr_dbg("write LAST_SAMPLE failed: %d\n", ret);
		AIKIT_End(sr->handle);
		return ret;
	}

	AIKIT_End(sr->handle);
	sr->handle = NULL;
	return 0;
}

int sr_write_audio_data(struct speech_rec *sr, char *data, unsigned int len)
{
	AiAudio *aiAudio_raw = NULL;
	int ret = 0;
	if (!sr)
		return -E_SR_INVAL;
	if (!data || !len)
		return 0;

	sr->dataBuilder->clear();
	aiAudio_raw = AiAudio::get("audio")->data(data, len)->status(sr->audio_status)->valid();
	sr->dataBuilder->payload(aiAudio_raw);

	printf("sr_write_audio_data\n");
	ret = ESRGetRlt(sr->handle, sr->dataBuilder);
	if (ret)
	{
		sr->audio_status = AIKIT_DataEnd;
		end_sr(sr);
		return ret;
	}
	sr->audio_status = AIKIT_DataContinue;

	return 0;
}

void sr_uninit(struct speech_rec *sr)
{
	if (sr->recorder)
	{
		if (!is_record_stopped(sr->recorder))
			stop_record(sr->recorder);
		close_recorder(sr->recorder);
		destroy_recorder(sr->recorder);
		sr->recorder = NULL;
	}

	if (sr->dataBuilder != nullptr)
	{
		delete sr->dataBuilder;
		sr->dataBuilder = nullptr;
	}
	if (sr->paramBuilder != nullptr)
	{
		delete sr->paramBuilder;
		sr->paramBuilder = nullptr;
	}
}