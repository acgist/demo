package com.acgist.face;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.face.EigenFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import com.acgist.utils.LocalPath;

/**
 * 人脸识别：不可用
 */
public class FaceJava {
	
	public static final AtomicInteger FACE_INDEX = new AtomicInteger(0); // 人脸index
	public static final String IMAGE_PATH = LocalPath.localPath("/image/meimei/"); // 图片位置
//	public static final String IMAGE_PATH = LocalPath.localPath("/image/");
	public static final String FACE_PATH = IMAGE_PATH + "face/"; // 人脸保存位置
	
	public static void main(String[] args) {
		System.load(LocalPath.localPath("/opencv/dll/opencv_java341.dll"));
		File[] imageFiles = imageFiles(IMAGE_PATH);
		Map<Integer, String> nameMapping = createSummary(imageFiles);
		int counter = 0;
		List<Mat> images = new ArrayList<>(imageFiles.length);
		MatOfInt labels = new MatOfInt(new int[imageFiles.length]);
		for (File image : imageFiles) {
			Mat inputMat = Imgcodecs.imread(image.getAbsolutePath(), Imgcodecs.CV_IMWRITE_JPEG_OPTIMIZE);
			int label = imageLabel(image.getName(), nameMapping);
			images.add(faceMat(inputMat));
			labels.put(counter++, 0, label);
		}
		EigenFaceRecognizer eigenFaceRecognizer = EigenFaceRecognizer.create();
//		FisherFaceRecognizer fisherFaceRecognizer = FisherFaceRecognizer.create();
//		LBPHFaceRecognizer lbphFaceRecognizer = LBPHFaceRecognizer.create();
		
		eigenFaceRecognizer.train(images, labels);
		
		Mat targetMat = Imgcodecs.imread(LocalPath.localPath("/image/csol01.jpg"), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		int label = eigenFaceRecognizer.predict_label(targetMat);
		System.out.println("targetMat: " + nameMapping.get(label));
	}
	
	public static Mat faceMat(Mat inputMat) {
		List<Mat> detectedElements = new ArrayList<Mat>(10);
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
			return null;
		}
		for (Rect rect : rects) {
			FACE_INDEX.addAndGet(1);
			
			Mat face = new Mat(grey.clone(), rect);
			detectedElements.add(resizeFace(face));
			Imgcodecs.imwrite(FACE_PATH + String.format("face-%04d", FACE_INDEX.get()) + ".jpg", resizeFace(face));
//			Imgcodecs.imwrite(FACE_PATH + String.format("face-%04d", FACE_INDEX.get()) + ".pgm", resizeFace(face));
			
//			Mat convert = new Mat();
//			Mat face = new Mat(grey.clone(), rect);
//			Imgproc.cvtColor(face, face, Imgproc.COLOR_BGR2GRAY);
//			Imgproc.equalizeHist(face, face);
//			face.convertTo(convert, Imgproc.COLOR_BGR2GRAY);
//			detectedElements.add(resizeFace(convert));
//			Imgcodecs.imwrite(FACE_PATH + String.format("face-%04d", FACE_INDEX.get()) + ".pgm", resizeFace(convert));
		}
		if (detectedElements.size() == 0) {
			detectedElements.add(resizeFace(inputMat));
		}
		return detectedElements.get(0);
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

	private static final int imageLabel(String filename, Map<Integer, String> nameMapping) {
		String name = filename.split("_")[0];
		return nameMapping.keySet().stream().filter(id -> nameMapping.get(id).equals(name)).findFirst().orElse(-1);
	}

	private static final Map<Integer, String> createSummary(File[] imageFiles) {
		Map<Integer, String> nameMapping = new HashMap<>(imageFiles.length);
		int counter = 0;
		for (File imageFile : imageFiles) {
			String name = imageFile.getName().split("_")[0];
			if (!nameMapping.values().contains(name)) {
				nameMapping.put(counter++, name);
			}
		}
		return nameMapping;
	}
	
}