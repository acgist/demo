/*
@file
@brief 基于录音接口和讯飞MSC接口封装一个MIC录音识别的模块

@author		taozhang9
@date		2016/05/27
*/

#include "aikit_biz_api.h"
#include "aikit_constant.h"
#include "aikit_biz_config.h"
#include "aikit_biz_builder.h"

enum sr_audsrc
{
	SR_MIC,	/* write data from mic */
	SR_USER	/* write data from user by calling API */
};

#define DEFAULT_INPUT_DEVID     (-1)


#define E_SR_NOACTIVEDEVICE		1
#define E_SR_NOMEM				2
#define E_SR_INVAL				3
#define E_SR_RECORDFAIL			4
#define E_SR_ALREADY			5

#define ESR_HAS_RESULT          6001

#define END_REASON_VAD_DETECT	0	/* detected speech done  */

struct speech_rec {
	enum sr_audsrc aud_src;  /* from mic or manual  stream write */
	AIKIT_HANDLE* handle;
	const char* ABILITY;
	int audio_status;
	struct recorder *recorder;
	volatile int state;
	AIKIT::AIKIT_DataBuilder* dataBuilder;
	AIKIT::AIKIT_ParamBuilder* paramBuilder;
};


#ifdef __cplusplus
extern "C" {
#endif

/* must init before start . devid = -1, then the default device will be used.
devid will be ignored if the aud_src is not SR_MIC */
int sr_init(struct speech_rec * sr, enum sr_audsrc aud_src, int devid);
int sr_start_listening(struct speech_rec *sr);
int sr_stop_listening(struct speech_rec *sr);
/* only used for the manual write way. */
int sr_write_audio_data(struct speech_rec *sr, char *data, unsigned int len);
/* must call uninit after you don't use it */
void sr_uninit(struct speech_rec * sr);

int ESRGetRlt(AIKIT_HANDLE* handle, AIKIT::AIKIT_DataBuilder* dataBuilder);

#ifdef __cplusplus
} /* extern "C" */	
#endif /* C++ */