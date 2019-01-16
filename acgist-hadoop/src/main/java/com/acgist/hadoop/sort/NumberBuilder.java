package com.acgist.hadoop.sort;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

public class NumberBuilder {

	public static void main(String[] args) {
		File file = new File("E:\\tmp\\sort\\number.txt");
		Random random = new Random();
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
			for (int index = 0; index < 10000000; index++) {
				writer.append(String.valueOf(random.nextInt(100)));
				writer.newLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
