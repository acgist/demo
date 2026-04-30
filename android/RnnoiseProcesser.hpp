#ifndef ANDROIDMEDIASOUP_RNNOISEPROCESSER_HPP
#define ANDROIDMEDIASOUP_RNNOISEPROCESSER_HPP

#include "limits"

#include "jni.h"
#include "android/log.h"

#ifndef RNNOISE_TAG
#define RNNOISE_TAG "rnnoise"
#endif

namespace xinyuanhe {

#include "rnnoise.h"

/**
 * 降噪配置
 */
class RnnoiseConfig {

public:
    // 采样位深：16
    int bits;
    // 数据大小：960
    int size;
    // 采样率：48000
    int rate;
    // 降噪数据大小
    int rnnoiseSize;
    // 降噪数据
    float* rnnoiseData;
    // 降噪对象
    DenoiseState* denoiseState;

public:
    RnnoiseConfig();
    virtual ~RnnoiseConfig();
    /**
     * 初始化
     */
    void init();

};

}

#endif // ANDROIDMEDIASOUP_RNNOISEPROCESSER_HPP
