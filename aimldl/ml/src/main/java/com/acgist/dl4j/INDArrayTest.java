package com.acgist.dl4j;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class INDArrayTest {

	public static void main(String[] args) {
		INDArray array = Nd4j.create(new double[] {1, 2, 3, 4, 5, 6}, new int[] {3, 2});
		System.out.println(array);
		array = array.add(2);
		System.out.println(array);
		array.addi(2);
		System.out.println(array);
		array.put(2, Nd4j.scalar(2));
		System.out.println(array);
	}
	
}
