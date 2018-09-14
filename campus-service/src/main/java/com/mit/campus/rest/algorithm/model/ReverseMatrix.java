package com.mit.campus.rest.algorithm.model;

/**
 * 回归算矩阵
 * @author : lw
 * @since : 2018-9-7
 */
public class ReverseMatrix {

private ReverseMatrix(){}

	// 矩阵求逆
	public static double[][] reverse(double[][] matrix) {
		double[][] temp;
		double[][] back_temp;
		// 得到矩阵的阶数
		int m_length = matrix.length;
		// 创建n*（2n-1）行列式，用来求逆矩阵，原矩阵和单位矩阵
		temp = new double[m_length][2 * m_length];
		// 创建返回的矩阵,初始化
		back_temp = matrix;
		// 将原矩阵的值赋给 temp矩阵，并添加单位矩阵的值
		for (int x = 0; x < m_length; x++) {
			for (int y = 0; y < temp[0].length; y++) {
				if (y > m_length - 1) {
					if (x == (y - m_length))
						temp[x][y] = 1;
					else
						temp[x][y] = 0;
				} else {
					temp[x][y] = matrix[x][y];
				}
			}
		}
		// System.out.println("组合矩阵:");
		// showMatrix(temp);
		// 高斯消元求逆矩阵
		for (int x = 0; x < m_length; x++) {
			double var = temp[x][x];
			// 判断对角线上元素是否为0，是的话与后面的行进行交换行，如没有满足条件的
			// 则可认为原矩阵没有逆矩阵。然后取值要化为0的列的值
			for (int w = x; w < temp[0].length; w++) {
				if (temp[x][x] == 0) {
					int k;
					for (k = x + 1; k < temp.length; k++) {
						if (temp[k][k] != 0) {
							for (int t = 0; t < temp[0].length; t++) {
								// System.out.println(">>>"+k+"<<<");
								double tmp = temp[x][t];
								temp[x][t] = temp[k][t];
								temp[k][t] = tmp;
							}
							break;
						}
					}
					// System.out.println(""+k);
					// 如果出现无法将temp矩阵的左边化为单位矩阵，返回原矩阵
					if (k >= temp.length)
						return back_temp;
					var = temp[x][x];
					// System.out.print("第 " + x + "次变换前替换主元上的 0");
					// System.out.println("(将 " + x + " 行与第 " + k + " 行进行交换)：");
					// showMatrix(temp);
				}
				temp[x][w] /= var;
			}
			// 将第x列的元素出对角线上的元素外都化为0，即构建单位矩阵
			for (int z = 0; z < m_length; z++) {
				double var_tmp = 0.0;
				for (int w = x; w < temp[0].length; w++) {
					// System.out.println("-"+x+"-"+z+"-"+w+"+++" + temp[z][w]);
					if (w == x)
						var_tmp = temp[z][x];
					if (x != z)
						temp[z][w] -= (var_tmp * temp[x][w]);
				}
			}
			// System.out.println("第 " + x + "次变换:");
			// showMatrix(temp);
		}
		// 取逆矩阵的值
		for (int x = 0; x < m_length; x++) {
			for (int y = 0; y < m_length; y++) {
				back_temp[x][y] = temp[x][y + m_length];
			}
		}
		return back_temp;
	}

	/*
	 * public static void showMatrix(double[][] ma) { int x = ma.length; int y =
	 * ma[0].length; for (int i = 0; i < x; i++) { for (int j = 0; j < y; j++)
	 * System.out.print("\t" + ma[i][j]); System.out.println(); } }
	 */

}