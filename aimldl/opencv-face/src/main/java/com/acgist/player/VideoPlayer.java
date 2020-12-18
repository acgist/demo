package com.acgist.player;

import java.util.concurrent.ExecutionException;

import javax.swing.WindowConstants;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;

import com.acgist.utils.LocalPath;

/**
 * javacv-platform
 */
public class VideoPlayer {

	private static final String DLL_PATH = "/opencv/dll/opencv_java341.dll";
	private static final String DLL_MEDIA_PATH = "/opencv/dll/opencv_ffmpeg341_64.dll";
	
	public static void main(String[] args) throws Exception, InterruptedException, ExecutionException {
		System.load(LocalPath.localPath(DLL_PATH));
		System.load(LocalPath.localPath(DLL_MEDIA_PATH));
		FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(LocalPath.localPath("/video/mayun.mp4"));
		grabber.start();
		int time = (int) (grabber.getLengthInTime() / 1000 / 1000);
		int totalFps = grabber.getLengthInVideoFrames();
		int fps = totalFps / time;
		showFrames("VideoPlayer", grabber, fps);
		grabber.stop();
		grabber.close();
	}
	
	private static final void showFrames(String winTitle, FrameGrabber grabber, int fps) throws Exception, InterruptedException {
		CanvasFrame canvas = new CanvasFrame(winTitle, 1); // 新建一个窗口
		canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		canvas.setAlwaysOnTop(true);
		int sleep = 1000 / fps;
		while (true) {
			if (!canvas.isVisible()) {
				break;
			}
			Frame frame = grabber.grab();
			canvas.showImage(frame);
			Thread.sleep(sleep); // 1000 / 帧数(20)
		}
	}

}
