package com.mit.campus.rest.algorithm.model;
/**
 * 矩阵相乘
 * @author ：lw
 * @since : 2018-9-7
 */
public class Multiply {
	private Multiply(){}
	public static int ah, bh, al, bl, ch, cl;
	public static double[][] c;
	public static double[][] multi(double[][] a,double[][] b){
		ah = a.length;
		al = a[0].length;
		bh = b.length;
		bl = b[0].length;
		c = new double[ah][bl];
		ch = c.length;
		cl = c[0].length;
		for (int i = 0; i < ch; i++) {
			for (int j = 0; j < cl; j++) {
				c[i][j] = sum(i, j, al, bh,a,b);
			}
		}

/*		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].length; j++) {
				System.out.print(c[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("");*/
		return c;
		
	}
	private static double sum(int i, int j, int al, int bh,double[][] a,double[][] b) {
		double sum = 0;
		int l = 0;
		for (int k = 0; k < al; k++) {
			if (l < bh) {
				sum += a[i][k] * b[l][j];
				l++;
			}
		}
		return sum;
	}
}
