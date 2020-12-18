package com.acgist.utils;

import java.math.BigDecimal;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * 特征工程
 */
public class Feature {

	/**
	 * 图片特征工程
	 * @param path 图片路径
	 */
	public static final String image(String path) {
		System.load(LocalPath.localPath("/opencv/dll/opencv_java341.dll"));
//		double array[][] = { { 2, 1, 0 }, { 1, 3, 1 }, { 0, 1, 2 } };
//		Mat mat = Mat.zeros(3, 3, opencv_core.CV_64FC1).asMat();
		Mat mat = opencv_imgcodecs.imread(path, opencv_imgcodecs.CV_LOAD_IMAGE_ANYDEPTH);
//		Mat mat = opencv_imgcodecs.imread(path, opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		mat = square(mat);
		mat.convertTo(mat, opencv_core.CV_64FC1);
		int row = mat.rows();
		int col = mat.cols();
//		for (int i = 0; i < mat.rows(); i++) {
//			for (int j = 0; j < mat.cols(); j++) {
//				mat.row(j).col(i).arrayData().putDouble(array[i][j]);
//				System.out.print(doubleValue(mat.row(j).col(i).arrayData().getDouble()) + " ");
//			}
//			System.out.println();
//		}
		Mat values = Mat.zeros(row, col, opencv_core.CV_64FC1).asMat();
		Mat vector = Mat.zeros(row, col, opencv_core.CV_64FC1).asMat();
		opencv_core.eigen(mat, values, vector);
//		打印输出
//		for (int i = 0; i < values.rows(); i++) {
//			for (int j = 0; j < values.cols(); j++) {
//				System.out.print(doubleValue(values.row(i).col(j).arrayData().getDouble()) + " ");
//			}
//			System.out.println();
//		}
//		for (int i = 0; i < vector.rows(); i++) {
//			for (int j = 0; j < vector.cols(); j++) {
//				System.out.print(doubleValue(vector.row(i).col(j).arrayData().getDouble()) + " ");
//			}
//			System.out.println();
//		}
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < vector.rows(); i++) {
			for (int j = 0; j < vector.cols(); j++) {
				content.append(doubleValue(vector.row(i).col(j).arrayData().getDouble())).append(",");
			}
		}
		content.setLength(content.length() - 1);
 		return content.toString();
	}
	
	public static final double doubleValue(double value) {
		return new BigDecimal(value).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static final Mat square(Mat mat) {
		int row = mat.rows();
		int col = mat.cols();
		int size;
		if(row > col) {
			size = col;
		} else {
			size = row;
		}
		mat = mat.apply(new Rect(0, 0, size, size));
//		opencv_imgproc.resize(mat, mat, new Size(200, 200));
		opencv_imgproc.resize(mat, mat, new Size(40, 40));
		opencv_imgcodecs.imwrite("e://tmp//" + System.currentTimeMillis() + ".jpg", mat);
		return mat;
	}
	
}
