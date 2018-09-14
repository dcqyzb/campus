package com.mit.campus.rest.algorithm.model;

/**
 * 多元回归
 * @author ： lw
 * @since ：2018年9月7日
 */
public class ManyResgress {
	private ManyResgress() {
	}

	static double[][] lastMatrix;
	static double[][] sequa;

	public static double[][] manyResgress(double[][] mainMatrix, double[] y) {
		int i;
		int j;
		int f = mainMatrix.length;
		int n = y.length;
		double[] a = new double[f + 1];
		double[] v = new double[f];
		double[] dt = new double[4];
		double[] tt = new double[f + 1];
		double[] cc = new double[n];
		double[] er = new double[n - 1];
		double DW1 = 0;
		double DW2 = 0;
		double DW = 0;
		String lastVal = "";
		// 将x矩阵新增一列，值为1
		double[][] TestMatrix = new double[f + 1][n];
		for (int g = 0; g <= f; g++) {
			for (int h = 0; h < n; h++) {
				if (g == 0) {
					TestMatrix[0][h] = 1.0;
				} else {
					TestMatrix[g][h] = mainMatrix[g - 1][h];
				}
			}
		}
		ManyResgress.sqt2(mainMatrix, y, f, n, a, dt, v, cc);
		// 打印变量系数
		for (i = 0; i <= f; i++) {
			System.out.println("a(" + i + ")=" + a[i]);
		}
		// 打印统计量R^2，应大于0.95
		System.out.println("R^2=" + dt[2]);
		// 打印统计量F，同F(4,5)相比，应远远大于该值
		System.out.println("F=" + (dt[3] * (n - f - 1)) / (f * dt[0]));
		// 打印统计量DW，值越靠近2，表示该模型越有效
		for (j = 0; j < n - 1; j++) {
			er[j] = cc[j + 1] - cc[j];
			DW1 = DW1 + (er[j]) * (er[j]);
		}
		for (j = 0; j < n; j++) {
			DW2 = DW2 + (cc[j]) * (cc[j]);
		}

		DW = DW1 / DW2;
		System.out.println("DW=" + DW);

		// 计算各变量系数估计值的T检验值，同t2/a(5)=2.015相比，低于该值得变量将剔除
		// 第一步，计算样本矩阵的转置
		double[][] tran = Transpose.tranpose(TestMatrix);
		// 第二步，将样本矩阵的转置矩阵通样本矩阵相乘
		double[][] multi = Multiply.multi(TestMatrix, tran);
		// 第三步，将上面的矩阵取逆
		double[][] rvs = ReverseMatrix.reverse(multi);
		// 第四步，t值等于b/(sqrt(RSS/(n-k-1)*diag(rvs)))
		int row_length = rvs.length;
		double sqrtValue = 0;
		for (int k = 1; k < row_length; k++) {
			sqrtValue = (dt[0] / (n - f - 1)) * (rvs[k][k]);
			tt[k] = a[k - 1] / Math.sqrt(sqrtValue);
			System.out.println("tt(" + k + ")=" + tt[k]);
			if (tt[k] > 2.015 || tt[k] < -2.015) {
				lastVal += k + "&";
			} else {
				System.out.println("变量" + k + "被剔除！");
			}
		}
		if(lastVal.length() == 0){
			System.out.println("变量被全部剔除！");
			return null;
		}else{
			int lastNum = lastVal.split("&").length;
			if (lastNum != f) {
				String[] lastString = lastVal.split("&");
				lastMatrix = new double[lastNum][n];
				for (int l = 0; l < lastNum; l++) {
					int index = Integer.parseInt(lastString[l]);
					lastMatrix[l] = mainMatrix[index - 1];
				}
				System.out.println("检验过滤后的矩阵为" + lastMatrix);
				return lastMatrix;
			} else {
				System.out.println("模型通过验证！");
				sequa = new double[1][a.length];
				for (int l = 0; l < a.length; l++) {
					sequa[0][l] = a[l];
				}
				return sequa;
			}
		}
	}

	private static void sqt2(double[][] x, double[] y, int m, int n, double[] a, double[] dt, double[] v, double[] cc) {

		int i, j, k, mm;
		double q, e, u, p, yy, s, r, pp;
		double[] b = new double[(m + 1) * (m + 1)];
		mm = m + 1;
		b[mm * mm - 1] = n;
		for (j = 0; j <= m - 1; j++) {
			p = 0.0;
			for (i = 0; i <= n - 1; i++)
				p = p + x[j][i];
			b[m * mm + j] = p;
			b[j * mm + m] = p;
		}
		for (i = 0; i <= m - 1; i++)
			for (j = i; j <= m - 1; j++) {
				p = 0.0;
				for (k = 0; k <= n - 1; k++)
					p = p + x[i][k] * x[j][k];
				b[j * mm + i] = p;
				b[i * mm + j] = p;
			}
		a[m] = 0.0;
		for (i = 0; i <= n - 1; i++)
			a[m] = a[m] + y[i];
		for (i = 0; i <= m - 1; i++) {
			a[i] = 0.0;
			for (j = 0; j <= n - 1; j++)
				a[i] = a[i] + x[i][j] * y[j];
		}
		chlk(b, mm, 1, a);
		yy = 0.0;
		for (i = 0; i <= n - 1; i++)
			yy = yy + y[i] / n;
		q = 0.0;
		e = 0.0;
		u = 0.0;
		for (i = 0; i <= n - 1; i++) {
			p = a[m];
			for (j = 0; j <= m - 1; j++)
				p = p + a[j] * x[j][i];
			cc[i] = (y[i] - p);
			q = q + (y[i] - p) * (y[i] - p);
			e = e + (y[i] - yy) * (y[i] - yy);
			u = u + (yy - p) * (yy - p);
		}
		s = Math.sqrt(q / n);
		r = Math.sqrt(1.0 - q / e);
		for (j = 0; j <= m - 1; j++) {
			p = 0.0;
			for (i = 0; i <= n - 1; i++) {
				pp = a[m];
				for (k = 0; k <= m - 1; k++)
					if (k != j)
						pp = pp + a[k] * x[k][i];
				p = p + (y[i] - pp) * (y[i] - pp);
			}
			v[j] = Math.sqrt(1.0 - q / p);
		}
		dt[0] = q;
		dt[1] = s;
		dt[2] = r;
		dt[3] = u;
	}

	private static int chlk(double[] a, int n, int m, double[] d) {
		int i, j, k, u, v;
		if ((a[0] + 1.0 == 1.0) || (a[0] < 0.0)) {
			System.out.println("fail\n");
			return (-2);
		}
		a[0] = Math.sqrt(a[0]);
		for (j = 1; j <= n - 1; j++)
			a[j] = a[j] / a[0];
		for (i = 1; i <= n - 1; i++) {
			u = i * n + i;
			for (j = 1; j <= i; j++) {
				v = (j - 1) * n + i;
				a[u] = a[u] - a[v] * a[v];
			}
			if ((a[u] + 1.0 == 1.0) || (a[u] < 0.0)) {
				System.out.println("fail\n");
				return (-2);
			}
			a[u] = Math.sqrt(a[u]);
			if (i != (n - 1)) {
				for (j = i + 1; j <= n - 1; j++) {
					v = i * n + j;
					for (k = 1; k <= i; k++)
						a[v] = a[v] - a[(k - 1) * n + i] * a[(k - 1) * n + j];
					a[v] = a[v] / a[u];
				}
			}
		}
		for (j = 0; j <= m - 1; j++) {
			d[j] = d[j] / a[0];
			for (i = 1; i <= n - 1; i++) {
				u = i * n + i;
				v = i * m + j;
				for (k = 1; k <= i; k++)
					d[v] = d[v] - a[(k - 1) * n + i] * d[(k - 1) * m + j];
				d[v] = d[v] / a[u];
			}
		}
		for (j = 0; j <= m - 1; j++) {
			u = (n - 1) * m + j;
			d[u] = d[u] / a[n * n - 1];
			for (k = n - 1; k >= 1; k--) {
				u = (k - 1) * m + j;
				for (i = k; i <= n - 1; i++) {
					v = (k - 1) * n + i;
					d[u] = d[u] - a[v] * d[i * m + j];
				}
				v = (k - 1) * n + k - 1;
				d[u] = d[u] / a[v];
			}
		}
		return (2);
	}
}
