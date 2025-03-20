#include <fstream>
#include <assert.h>
#include <cstring>
#include <atomic>

// #include <unistd.h>

#include <Windows.h>

#include "include/aikit_biz_api.h"
#include "include/aikit_constant.h"
#include "include/aikit_biz_config.h"
#include "include/aikit_biz_builder.h"

using namespace std;
using namespace AIKIT;

static std::atomic_bool ttsFinished(false);
static const int TIME_ONE_DAY_SECONDS = 24 * 60 * 60;

void OnOutput(AIKIT_HANDLE *handle, const AIKIT_OutputData *output) {
	printf("OnOutput key:%s\n", (char *)output->node->key);
	printf("OnOutput status:%d\n", output->node->status);
	if (output->node->value) {
		FILE *fin = fopen("./OutPut.pcm", "ab");
		fwrite(output->node->value, sizeof(char), output->node->len, fin);
		fclose(fin);
	}
}

void OnEvent(AIKIT_HANDLE *handle, AIKIT_EVENT eventType, const AIKIT_OutputEvent *eventValue) {
	printf("OnEvent:%d\n", eventType);
	if (eventType == AIKIT_Event_End) {
		ttsFinished = true;
	}
}

void OnError(AIKIT_HANDLE *handle, int32_t err, const char *desc) {
	printf("OnError:%d\n", err);
}

int TestXiaoyan(AIKIT_ParamBuilder *paramBuilder, AIKIT_DataBuilder *dataBuilder) {
	string text = "技术支持跑的快,全靠大佬们带,受着老板们和研发大佬熏陶,技术支持也不会掉队的.";
	AIKIT_HANDLE *handle = nullptr;
	ttsFinished = false;
	const char *role = "xiaoyan"; // xiaofeng xiaoyan
	paramBuilder = AIKIT_ParamBuilder::create();
	paramBuilder->clear();
	paramBuilder->param("vcn", role, strlen(role));				   // 必选参数，60020：xiaoyan发音人60030：xiaofeng发音人69010：john英文发音人69020：catherine英文发音人
	paramBuilder->param("vcnModel", role, strlen(role));		   // 必选参数，取值与vcn保持一致
	paramBuilder->param("language", 1);							   // 必须参数，1是中文，2是英文，是英文时，对应文本也得是英文
	paramBuilder->param("textEncoding", "UTF-8", strlen("UTF-8")); // 可选参数，文本编码格式，默认为65001，UTF8格式
	dataBuilder = AIKIT_DataBuilder::create();
	AiText *aiText = AiText::get("text")->encoding(AiText::ENCODING_UTF8)->compress(AiText::COMPRESS_RAW)->format("json")->status(3)->data(text.c_str(), text.length())->valid();
	dataBuilder->payload(aiText);
	int ret = AIKIT_Start("e2e44feff", AIKIT_Builder::build(paramBuilder), nullptr, &handle);
	printf("AIKIT_Start:%d\n", ret);
	if (ret != 0) {
		return ret;
	}
	ret = AIKIT_Write(handle, AIKIT_Builder::build(dataBuilder));
	printf("AIKIT_Write:%d\n", ret);
	if (ret != 0) {
		return ret;
	}
	while (ttsFinished != true) {
		Sleep(1000);
	}
	ret = AIKIT_End(handle);
	delete dataBuilder;
	dataBuilder = nullptr;
	return ret;
}

void TestXTTS() {
	AIKIT_ParamBuilder *paramBuilder = nullptr;
	AIKIT_DataBuilder *dataBuilder = nullptr;
	AIKIT_OutputData *output = nullptr;
	AIKIT_HANDLE *handle = nullptr;
	int loopCount = 1;
	AIKIT_Callbacks cbs = {OnOutput, OnEvent, OnError};
	AIKIT_RegisterCallback(cbs);
	AIKIT_SetLogInfo(0, LOG_STDOUT, nullptr);
	// 此接口用来设置授权校验的间隔时间（即多久时间会去校验授权一次）
	// AIKIT_SetAuthCheckInterval(TIME_ONE_DAY_SECONDS);
	AIKIT_Configurator::builder()
		.log()
		.logLevel(LOG_LVL_INFO)
		.logMode(2)
		.logPath("log.txt")
		.app()
		.appID("")
		.apiSecret("")
		.apiKey("")
		.workDir("./")
		.resDir("./resource")
		.auth()
		.authType(0)
		.ability("e2e44feff");

	int ret = AIKIT_Init();
	if (ret != 0) {
		printf("AIKIT_Init failed:%d\n", ret);
		goto exit;
	}
	while (loopCount--) {
		ret = TestXiaoyan(paramBuilder, dataBuilder);
		if (ret != 0) {
			printf("TestXiaoyan failed %d\n", ret);
			goto exit;
		}
	}

exit:
	if (paramBuilder != nullptr) {
		delete paramBuilder;
		paramBuilder = nullptr;
	}
	if (dataBuilder != nullptr) {
		delete dataBuilder;
		dataBuilder = nullptr;
	}
	// 程序真正退出时，才调用AIKIT_UnInit释放所有资源
	// sleep(20);
	AIKIT_UnInit();
}

int main() {
	system("chcp 65001");
	TestXTTS();
	system("pause");
	return 0;
}