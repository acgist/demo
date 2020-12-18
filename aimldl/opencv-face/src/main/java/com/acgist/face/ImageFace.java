package com.acgist.face;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import com.acgist.utils.LocalPath;

/**
 * 人脸探测
 */
public class ImageFace {

	private static final String DLL_PATH = "/opencv/dll/opencv_java341.dll";
	private static final String XML_PATH = "/opencv/xml/haarcascade_frontalface_alt.xml";
	
	public static void main(String[] args) throws URISyntaxException, IOException {
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.load(LocalPath.localPath(DLL_PATH));
		Mat image = openImage("/image/csol01.jpg");
//		Mat image = openImage("/image/崔智云.jpg");
		CascadeClassifier faceDetector = new CascadeClassifier(LocalPath.localPath(XML_PATH));
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);
		for (Rect rect : faceDetections.toArray()) {
			Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}
		String filename = "ouput.png";
		Imgcodecs.imwrite(filename, image);
		System.out.println("over");
	}
	
	public static final Mat openImage(String name) throws IOException {
//		不能读取含有中文的文件
//		String path = FaceRecog.class.getResource(name).getPath().substring(1);
//		Mat image = Imgcodecs.imread(path);
//		解决中文问题
		InputStream input = ImageFace.class.getResourceAsStream(name);
		BufferedImage src = ImageIO.read(input);
		Mat image = new Mat(src.getHeight(), src.getWidth(), CvType.CV_8UC3);
		image.put(0, 0, ((DataBufferByte) src.getRaster().getDataBuffer()).getData());
		return image;
	}
	
	/**
	 * Mat转换BufferedImage
	 */
	public static final void mat2image(Mat mat) throws IOException {
		String ext = ".jpg"; // 默认JPG
		String formatName = ext.substring(1);
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(ext, mat, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		InputStream input = new ByteArrayInputStream(byteArray);
		BufferedImage image = ImageIO.read(input);
		ImageIO.write(image, formatName, new FileOutputStream("./mat2image" + ext));
	}

}
