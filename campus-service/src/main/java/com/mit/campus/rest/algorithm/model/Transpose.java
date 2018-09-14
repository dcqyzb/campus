package com.mit.campus.rest.algorithm.model;
/**
 * 矩阵转置
 * @author ：lw
 * @since : 2018-9-7
 */
public class Transpose {
	private Transpose(){}
	public static double[][] MatrixC;
	public static double[][] tranpose(double[][] TestMatrix){
		int Line = TestMatrix.length;
		int List = TestMatrix[0].length;
		double[][] MatrixC = new double[List][Line];
		for (int i = 0; i < Line; i++) {
			for (int j = 0; j < List; j++) {
				MatrixC[j][i] = TestMatrix[i][j];
			}
		}
		return MatrixC;
	}
}
