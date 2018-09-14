package com.mit.campus.rest.algorithm.model;

/**
 * 
* 一元回归
* @author shuyy
* @date 2018年9月10日
 */
public class OneResgress {
	private OneResgress(){}
	public static String oneResgress(double[][] lastMatrix,double[] y){
		double[] dt = new double[6];
		double[] a = new double[2];
		int n = y.length;
		String sequation = "";
		//double[] x = { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0 };
		//double[] y = { 2.75, 2.84, 2.965, 3.01, 3.20, 3.25, 3.38, 3.43, 3.55, 3.66, 3.74 };
		OneResgress.SPT1(lastMatrix[0], y, n, a, dt);
		System.out.println("");
		System.out.println("a=" + a[1] + " b=" + a[0]);
		System.out.println("q=" + dt[0] + " s=" + dt[1] + " p=" + dt[2]);
		System.out.println(" umax=" + dt[3] + " umin=" + dt[4] + " u=" + dt[5]);
		sequation = "y=" + a[1] + "x+" + a[0];
		return sequation;
	}
	//一元线性回归
		public static void SPT1(double[] x, double[] y, int n, double[] a, double[] dt) {
			int i;
			double xx, yy, e, f, q, u, p, umax, umin, s;
			xx = 0.0;
			yy = 0.0;
			for (i = 0; i <= n - 1; i++) {
				xx = xx + x[i] / n;
				yy = yy + y[i] / n;
			}
			e = 0.0;
			f = 0.0;
			for (i = 0; i <= n - 1; i++) {
				q = x[i] - xx;
				e = e + q * q;
				f = f + q * (y[i] - yy);
			}
			a[1] = f / e;
			a[0] = yy - a[1] * xx;
			q = 0.0;
			u = 0.0;
			p = 0.0;
			umax = 0.0;
			umin = 1.0e+30;
			for (i = 0; i <= n - 1; i++) {
				s = a[1] * x[i] + a[0];
				q = q + (y[i] - s) * (y[i] - s);
				p = p + (s - yy) * (s - yy);
				e = Math.abs(y[i] - s);
				if (e > umax)
					umax = e;
				if (e < umin)
					umin = e;
				u = u + e / n;
			}
			dt[1] = Math.sqrt(q / n);
			dt[0] = q;
			dt[2] = p;
			dt[3] = umax;
			dt[4] = umin;
			dt[5] = u;
		}
}
