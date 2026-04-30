package com.acgist.snail.codec;

import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import java.io.File;

/**
 * 视频转码
 */
public class MediaUtils {

    private static final String TAG = "MediaUtils";

    private static final int RETURN_CODE_SUCCESS = 0;
    private static final int RETURN_CODE_FAIL    = -1;

    /**
     * 视频转码替换
     *
     * @param path 文件路径
     */
    public static final Boolean transcode(String path) {
        final File file = new File(path);
        if(!file.exists() || !file.isFile()) {
            Log.d(TAG, "视频转码文件无效：" + path);
            return false;
        }
        Log.d(TAG, "视频转码开始：" + path);
        final String outputPath = path + ".mp4";
        final File   outputFile = new File(outputPath);
        final int code = muxer(path, outputPath);
//      final int code = ffmpeg(path, outputPath);
        if(code == RETURN_CODE_SUCCESS) {
            if(outputFile.renameTo(file)) {
                Log.d(TAG, "视频转码完成：" + path);
                return true;
            } else {
                Log.w(TAG, "视频转换失败（替换）：" + path);
                outputFile.delete();
                return false;
            }
        } else {
            Log.w(TAG, "视频转码失败：" + path + " = " + code);
            if(outputFile.exists()) {
                outputFile.delete();
            }
            return false;
        }
    }

    public static final int muxer(String path, String outputPath) {
        final MediaTranscode mediaTranscode = new MediaTranscode();
        if(mediaTranscode.transcode(path, outputPath)) {
            return RETURN_CODE_SUCCESS;
        } else {
            return RETURN_CODE_FAIL;
        }
    }

    public static final int ffmpeg(String path, String outputPath) {
        return FFmpeg.execute(String.format("-y -i %s -c:a copy -c:v copy %s", path, outputPath));
    }

}
