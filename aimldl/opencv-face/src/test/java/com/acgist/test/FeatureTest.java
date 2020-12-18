package com.acgist.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.acgist.utils.Feature;

public class FeatureTest {

	public static void main(String[] args) throws IOException {
		String path = "E:\\git\\aimldl\\SparkMLlib\\src\\main\\resources\\cluster";
		File[] images = new File(path).listFiles();
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./vector.txt")));
		for (File file : images) {
			String result = Feature.image(file.getAbsolutePath());
			writer.write(file.getName());
			writer.newLine();
			writer.write(result);
			writer.newLine();
		}
		writer.close();
		System.out.println("over");
	}
	
}
