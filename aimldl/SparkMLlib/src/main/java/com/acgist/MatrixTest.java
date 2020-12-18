package com.acgist;

import org.apache.spark.mllib.linalg.BLAS;
import org.apache.spark.mllib.linalg.DenseMatrix;
import org.apache.spark.mllib.linalg.DenseVector;
import org.junit.Test;

public class MatrixTest {

	@Test
	public void matrix() {
		DenseMatrix matrix = DenseMatrix.zeros(2, 3);
		System.out.println(matrix);
		matrix = DenseMatrix.eye(3);
		System.out.println(matrix);
	}

	@Test
	public void vector() {
		DenseVector vector = new DenseVector(new double[] {1D, 2D, 3D, 4D});
		System.out.println(vector);
		DenseMatrix matrix = DenseMatrix.diag(vector);
		System.out.println(matrix);
	}

	@Test
	public void blas() {
		DenseVector vector = new DenseVector(new double[] {1D, 2D, 3D, 4D});
		System.out.println(vector);
		double value = BLAS.dot(vector, vector);
		System.out.println(value);
	}

}
