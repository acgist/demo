package com.acgist.face;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_face.EigenFaceRecognizer;
import org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.javacpp.opencv_imgcodecs;

import com.acgist.utils.LocalPath;

/**
 * 人脸识别
 */
public class FaceID {

	private static final String FACE_PATH = CreateFace.FACE_PATH;
	private static final List<String> IMAGES = new ArrayList<>(); // 训练数据
	private static final List<String> TEST_IMAGES = new ArrayList<>(); // 测试数据-训练时排除这些数据
	
	static {
		TEST_IMAGES.add(LocalPath.localPath("/image/meimei/face/face-0009.jpg"));
		TEST_IMAGES.add(LocalPath.localPath("/image/meimei/face/face-0010.jpg"));
		TEST_IMAGES.add(LocalPath.localPath("/image/meimei/face/face-0011.jpg"));
		File floder = new File(FACE_PATH);
		String path = null;
		for (File image : floder.listFiles()) {
			path = image.getAbsolutePath();
			if(!TEST_IMAGES.contains(path)) {
				IMAGES.add(path);
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
//		FaceID.train();
		FaceID.label();
	}
	
	/**
	 * 人脸置信度
	 */
	public static final void train() throws IOException {
		EigenFaceRecognizer eigenFaceRecognizer = EigenFaceRecognizer.create();
//		FisherFaceRecognizer fisherFaceRecognizer = FisherFaceRecognizer.create(); // 训练时必须大于等于两个label
		LBPHFaceRecognizer lbphFaceRecognizer = LBPHFaceRecognizer.create();
		int index = 0;
		int[] labes = new int[IMAGES.size()];
		MatVector src = new MatVector();
		for (String image : IMAGES) {
			System.out.println("开始训练：" + image);
//			src.push_back(opencv_imgcodecs.imread(image, opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE));
			src.push_back(opencv_imgcodecs.imread(image, opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE));
			labes[index++] = 1;
		}
		eigenFaceRecognizer.train(src, new Mat(labes));
//		fisherFaceRecognizer.train(src, new Mat(labes));
		lbphFaceRecognizer.train(src, new Mat(labes));
		
		// 保存训练模型
		eigenFaceRecognizer.save("eigenFace.xml");
		lbphFaceRecognizer.save("lbphFace.xml");
		
		// 加载训练模型
//		eigenFaceRecognizer = EigenFaceRecognizer.create();
//		eigenFaceRecognizer.read("eigenFace.xml");
//		lbphFaceRecognizer = LBPHFaceRecognizer.create();
//		lbphFaceRecognizer.read("lbphFace.xml");
		
		// 图片识别
//		IntBuffer label = IntBuffer.allocate(1);
//		DoubleBuffer confidence = DoubleBuffer.allocate(1);
		int [] label = new int[1];
		double [] confidence = new double[1];
		for (String path : TEST_IMAGES) {
			eigenFaceRecognizer.predict(opencv_imgcodecs.imread(path, opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE), label, confidence);
			System.out.print("测试图片：" + path);
			System.out.print("，标签（label）：" + label[0]);
			System.out.println("，置信度（confidence）：" + confidence[0]);
		}
		for (String path : TEST_IMAGES) {
			lbphFaceRecognizer.predict(opencv_imgcodecs.imread(path, opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE), label, confidence);
			System.out.print("测试图片：" + path);
			System.out.print("，标签（label）：" + label[0]);
			System.out.println("，置信度（confidence）：" + confidence[0]);
		}
//		System.out.println("label：" + eigenFaceRecognizer.predict_label(opencv_imgcodecs.imread("E://face85.jpg", opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE)));
	}
	
	/**
	 * 打标签
	 */
	public static final void label() {
		EigenFaceRecognizer eigenFaceRecognizer = EigenFaceRecognizer.create();
//		FisherFaceRecognizer fisherFaceRecognizer = FisherFaceRecognizer.create(); // 训练时必须大于等于两个label
		LBPHFaceRecognizer lbphFaceRecognizer = LBPHFaceRecognizer.create();
		int index = 0;
		IMAGES.addAll(TEST_IMAGES); // 添加测试数据
		int[] labes = new int[IMAGES.size()];
		MatVector src = new MatVector();
		for (String image : IMAGES) {
			System.out.println("开始训练：" + image);
//			src.push_back(opencv_imgcodecs.imread(image, opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE));
			src.push_back(opencv_imgcodecs.imread(image, opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE));
			if(image.contains("face-0010")) {
				labes[index++] = 2;
			} else if(image.contains("face-0011")) {
				labes[index++] = 3;
			} else {
				labes[index++] = 1;
			}
		}
		eigenFaceRecognizer.train(src, new Mat(labes));
//		fisherFaceRecognizer.train(src, new Mat(labes));
		lbphFaceRecognizer.train(src, new Mat(labes));
		
		// 保存训练模型
		eigenFaceRecognizer.save("eigenFace.xml");
		lbphFaceRecognizer.save("lbphFace.xml");
		
		// 加载训练模型
//		eigenFaceRecognizer = EigenFaceRecognizer.create();
//		eigenFaceRecognizer.read("eigenFace.xml");
//		lbphFaceRecognizer = LBPHFaceRecognizer.create();
//		lbphFaceRecognizer.read("lbphFace.xml");
		
		// 图片识别
		for (String path : TEST_IMAGES) {
			System.out.println("label：" + eigenFaceRecognizer.predict_label(opencv_imgcodecs.imread(path, opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE)));
		}
	}

}
