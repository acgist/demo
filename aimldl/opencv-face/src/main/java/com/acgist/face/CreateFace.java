package com.acgist.face;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.atomic.AtomicInteger;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import com.acgist.utils.LocalPath;

/**
 * 提取人脸数据<br>
 * 请使用英文名称和路径
 */
public class CreateFace {
	
	public static final AtomicInteger FACE_INDEX = new AtomicInteger(0); // 人脸index
	public static final String IMAGE_PATH = LocalPath.localPath("/image/meimei/"); // 图片位置
//	public static final String IMAGE_PATH = LocalPath.localPath("/image/");
	public static final String FACE_PATH = IMAGE_PATH + "face/"; // 人脸保存位置

	static {
		File face = new File(FACE_PATH);
		if(!face.exists()) {
			System.out.println("face目录不存在，自动创建");
			face.mkdirs();
		}
	}
	
	public static void main(String[] args) {
		System.load(LocalPath.localPath("/opencv/dll/opencv_java341.dll"));
		File[] imageFiles = imageFiles(IMAGE_PATH);
		for (File image : imageFiles) {
			Mat inputMat = Imgcodecs.imread(image.getAbsolutePath(), Imgcodecs.CV_IMWRITE_JPEG_OPTIMIZE);
			faceMat(inputMat);
		}
	}
	
	public static final void faceMat(Mat inputMat) {
		Mat rgba = new Mat();
		Mat grey = new Mat();
		inputMat.copyTo(rgba);
		inputMat.copyTo(grey);
		MatOfRect matOfRect = new MatOfRect();
		Imgproc.cvtColor(rgba, grey, Imgproc.COLOR_BGR2GRAY); // 图像灰度化
		Imgproc.equalizeHist(grey, grey); // 直方图均衡化，图像增强：暗的变亮，亮的变暗
		CascadeClassifier cascadeClassifier = new CascadeClassifier(LocalPath.localPath("/opencv/xml/haarcascade_frontalface_alt.xml"));
		cascadeClassifier.detectMultiScale(grey, matOfRect);
		Rect[] rects = matOfRect.toArray();
		if(rects.length != 1) {
			System.out.println("当前图片包含人脸数量: " + rects.length + "，跳过处理");
			return;
		}
		for (Rect rect : rects) {
			FACE_INDEX.addAndGet(1);
			
			Mat face = new Mat(grey.clone(), rect);
//			Imgproc.cvtColor(face, face, Imgproc.COLOR_BGR2GRAY);
//			Imgproc.equalizeHist(face, face);
			Imgcodecs.imwrite(FACE_PATH + String.format("face-%04d", FACE_INDEX.get()) + ".jpg", resizeFace(face));
//			Imgcodecs.imwrite(FACE_PATH + String.format("face-%04d", FACE_INDEX.get()) + ".pgm", resizeFace(face));
			
//			Mat convert = new Mat();
//			Mat face = new Mat(grey.clone(), rect);
//			Imgproc.cvtColor(face, face, Imgproc.COLOR_BGR2GRAY);
//			Imgproc.equalizeHist(face, face);
//			face.convertTo(convert, Imgproc.COLOR_BGR2GRAY);
//			Imgcodecs.imwrite(FACE_PATH + String.format("%04d", FACE_INDEX.get()) + ".jpg", resizeFace(convert));
		}
	}

	public static Mat resizeFace(Mat originalImage) {
		Mat resizedImage = new Mat();
		Imgproc.resize(originalImage, resizedImage, new Size(92D, 112D));
		return resizedImage;
	}

	private static File[] imageFiles(String imagePath) {
		File root = new File(imagePath);
		FilenameFilter filter = (dir, name) -> {
			name = name.toLowerCase();
			if(!name.matches("[a-zA-Z0-9.]+")) { // 过滤中文
				System.out.println("不符合规则的图片名称：" + name);
				return false;
			}
			return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
		};
		return root.listFiles(filter);
	}
	
}