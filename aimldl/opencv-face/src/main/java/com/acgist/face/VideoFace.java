package com.acgist.face;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.HOGDescriptor;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import com.acgist.utils.LocalPath;

/**
 * 视频人脸探测
 */
public class VideoFace extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final String DLL_PATH = "/opencv/dll/opencv_java341.dll";
	private static final String DLL_MEDIA_PATH = "/opencv/dll/opencv_ffmpeg341_64.dll";
	private static final String XML_PATH = "/opencv/xml/haarcascade_frontalface_alt.xml";

	private BufferedImage image;

	public static void main(String[] args) throws Exception {
		System.load(LocalPath.localPath(DLL_PATH));
		System.load(LocalPath.localPath(DLL_MEDIA_PATH));
		Mat mat = new Mat();
		VideoCapture capture = new VideoCapture(LocalPath.localPath("/video/mayun.mp4")); // 视频
//		VideoCapture capture = new VideoCapture(0); // 摄像头
		int height = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
		int width = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
		if (height == 0 || width == 0) {
			throw new Exception("没有数据源！");
		}
		JFrame frame = new JFrame("VideoFace");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		VideoFace panel = new VideoFace();
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				System.out.println("mouseClicked");
			}
			@Override
			public void mouseMoved(MouseEvent event) {
				System.out.println("mouseMoved");
			}
			@Override
			public void mouseReleased(MouseEvent event) {
				System.out.println("mouseReleased");
			}
			@Override
			public void mousePressed(MouseEvent event) {
				System.out.println("mousePressed");
			}
			@Override
			public void mouseExited(MouseEvent event) {
				System.out.println("mouseExited");
			}
			@Override
			public void mouseDragged(MouseEvent event) {
				System.out.println("mouseDragged");
			}
		});
		frame.setContentPane(panel);
		frame.setVisible(true);
		frame.setSize(width + frame.getInsets().left + frame.getInsets().right, height + frame.getInsets().top + frame.getInsets().bottom);
		int index = 0;
		Mat temp = new Mat();
		while (frame.isShowing() && index < 500) {
			capture.read(mat);
			Imgproc.cvtColor(mat, temp, Imgproc.COLOR_RGB2GRAY);
			panel.image = panel.mat2BI(detectFace(mat));
			panel.repaint();
		}
		capture.release();
		frame.dispose();
		frame.setResizable(false);
	}

	private BufferedImage mat2BI(Mat mat) {
		int dataSize = mat.cols() * mat.rows() * (int) mat.elemSize();
		byte[] data = new byte[dataSize];
		mat.get(0, 0, data);
		int type = mat.channels() == 1 ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR;
		if (type == BufferedImage.TYPE_3BYTE_BGR) {
			for (int index = 0; index < dataSize; index += 3) {
				byte blue = data[index + 0];
				data[index + 0] = data[index + 2];
				data[index + 2] = blue;
			}
		}
		BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
		image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
		return image;
	}

	public void paintComponent(Graphics graphics) {
		if (image != null) {
			graphics.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
		}
	}

	/**
	 * 人脸识别
	 */
	private static final Mat detectFace(Mat mat) throws Exception {
		CascadeClassifier faceDetector = new CascadeClassifier(LocalPath.localPath(XML_PATH));
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(mat, faceDetections);
		Rect[] rects = faceDetections.toArray();
		if (rects != null && rects.length >= 1) {
			for (Rect rect : rects) {
				Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 2);
			}
		}
		return mat;
	}
	
	/**
	 * 人型识别
	 */
	@SuppressWarnings("unused")
	private static final Mat detectPeople(Mat mat) {
		HOGDescriptor hog = new HOGDescriptor();
		hog.setSVMDetector(HOGDescriptor.getDefaultPeopleDetector());
		MatOfRect regions = new MatOfRect();
		MatOfDouble foundWeights = new MatOfDouble();
		hog.detectMultiScale(mat, regions, foundWeights);
		for (Rect rect : regions.toArray()) {
			Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 2);
		}
		return mat;
	}
	
}
