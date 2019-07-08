package elyazisitanima;

import java.util.Random;

public class Array {

	private static Random random;
	private static long seed;

	static {
		seed = System.currentTimeMillis();
		random = new Random(seed);
	}

	public static void setSeed(long s) {
		seed = s;
		random = new Random(seed);
	}

	public static long getSeed() {
		return seed;
	}

	public static double uniform() {
		return random.nextDouble();
	}

	public static int uniform(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("argument must be positive: " + n);
		}
		return random.nextInt(n);
	}

	public static long uniform(long n) {
		if (n <= 0L) {
			throw new IllegalArgumentException("argument must be positive: " + n);
		}

		long r = random.nextLong();
		long m = n - 1;

		// power of two
		if ((n & m) == 0L) {
			return r & m;
		}

		// reject over-represented candidates
		long u = r >>> 1;
		while (u + m - (r = u % n) < 0L) {
			u = random.nextLong() >>> 1;
		}
		return r;
	}

	public static int uniform(int a, int b) {
		/**
		 * if ((b <= a) || ((long) b - a >= Integer.MAX_VALUE)) { throw new
		 * IllegalArgumentException("invalid range: [" + a + ", " + b + ")"); }
		 */
		return a + uniform(b - a);
	}

	public static double uniform(double a, double b) {
		if (!(a < b)) {
			throw new IllegalArgumentException("invalid range: [" + a + ", " + b + ")");
		}
		return a + uniform() * (b - a);
	}

	public static double[] random(int m, double x, double y) {
		double[] a = new double[m];
		for (int i = 0; i < m; i++) {
			a[i] = uniform(x, y);
		}
		return a;
	}

	public static double[][] random(int m, int n, double x, double y) // x ile y arasýnda uniform daðýlan m ve n
																		// boyutlarýnda matrix üretir.
	{
		double[][] a = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				a[i][j] = uniform(x, y);
			}
		}
		return a;
	}

	public static double[][][] random(int m, int n, int o, double x, double y) {
		double[][][] a = new double[m][n][o];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				for (int z = 0; z < o; z++)
					a[i][j][z] = uniform(0.0, 1.0);
			}
		}
		return a;
	}

	public static double[][] T(double[][] a) {
		int m = a.length;
		int n = a[0].length;
		double[][] b = new double[n][m];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				b[j][i] = a[i][j];
			}
		}
		return b;
	}

	public static double[][] add(double[][] a, double[][] b) {
		int m = a.length;
		int n = a[0].length;
		double[][] c = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				c[i][j] = a[i][j] + b[i][j];
			}
		}
		return c;
	}
	public static double[] add(double[] a, double[] b) {
		int m = a.length;
		
		double[] c = new double[m];
		for (int i = 0; i < m; i++) {
			
				c[i] = a[i] + b[i];
			
		}
		return c;
	}

	public static double[][][] add(double[][][] a, double[][][] b) {
		int m = a.length;
		int n = a[0].length;
		int o = a[0][0].length;
		
		double[][][] c = new double[m][n][o];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < o; k++) {
					c[i][j][k] = a[i][j][k] + b[i][j][k];
				}
			}
		}
		return c;
	}
	
	public static double[][] add(double[][] a, double[] b) {
		int m = a.length;
		int n = a[0].length;
		double[][] c = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				c[i][j] = a[i][j] + b[j];
			}
		}
		return c;
	}

	public static int findMaxValIndex(double[] result) {
		double maxVal = 0;
		int index = 0;
		int size = result.length;

		for (int i = 0; i < size; i++) {
			if (result[i] > maxVal) {
				maxVal = result[i];
				index = i;
			}
		}
		return index;
	}

	public static double[][] subtract(double[][] a, double[][] b) {
		int m = a.length;
		int n = a[0].length;
		double[][] c = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				c[i][j] = a[i][j] - b[i][j];
			}
		}
		return c;
	}

	public static double[] subtract(double[] a, double[] b) {
		int m = a.length;
		double[] c = new double[m];

		for (int i = 0; i < m; i++) {

			c[i] = a[i] - b[i];

		}
		return c;
	}

	public static double[][] subtract(double a, double[][] b) {
		int m = b.length;
		int n = b[0].length;
		double[][] c = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				c[i][j] = a - b[i][j];
			}
		}
		return c;
	}

	public static double[] subtract(double a, double[] b) {
		int m = b.length;

		double[] c = new double[m];
		for (int i = 0; i < m; i++) {

			c[i] = a - b[i];

		}
		return c;
	}

	public static double[][][] subtract(double[][][] a, double[][][] b) {
		int m = b.length;
		int n = b[0].length;
		int z = b[0][0].length;
		double[][][] c = new double[m][n][z];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				for (int t = 0; t < z; t++) {
					c[i][j][t] = a[i][j][t] - b[i][j][t];
				}
			}
		}
		return c;
	}

	public static double[][] dot(double[][] a, double[][] b) {
		int m1 = a.length;
		int n1 = a[0].length;
		int m2 = b.length;
		int n2 = b[0].length;
		/**
		 * if (n1 != m2) { throw new RuntimeException("Illegal matrix dimensions."); }
		 */
		double[][] c = new double[m1][n2];
		for (int i = 0; i < m1; i++) {
			for (int j = 0; j < n2; j++) {
				for (int k = 0; k < n1; k++) {
					c[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return c;
	}

	public static double[][] multiply(double[][] x, double[][] a) {
		int m = a.length;
		int n = a[0].length;

		double[][] y = new double[m][n];
		for (int j = 0; j < m; j++) {
			for (int i = 0; i < n; i++) {
				y[j][i] = a[j][i] * x[j][i];
			}
		}

		return y;
	}

	public static double[][] multiply(double[] x, double[][] a) {
		int m = a.length;
		int n = a[0].length;

		double[][] y = new double[m][n];
		for (int j = 0; j < m; j++) {
			for (int i = 0; i < n; i++) {
				y[j][i] = a[j][i] * x[i];
			}
		}
		return y;
	}

	public static double[][] multiply(double x, double[][] a) {
		int m = a.length;
		int n = a[0].length;

		double[][] y = new double[m][n];
		for (int j = 0; j < m; j++) {
			for (int i = 0; i < n; i++) {
				y[j][i] = a[j][i] * x;
			}
		}
		return y;
	}

	public static double[] multiply(double x, double[] a) {
		int m = a.length;

		double[] y = new double[m];
		for (int j = 0; j < m; j++) {

			y[j] = a[j] * x;
		}
		return y;
	}

	public static double[] multiply(double[] x, double[] a) {
		int m = a.length;

		double[] y = new double[m];
		for (int j = 0; j < m; j++) {

			y[j] = a[j] * x[j];
		}
		return y;
	}

	public static double[][][] multiply(double x, double[][][] a) {
		int m = a.length;
		int n = a[0].length;
		int z = a[0][0].length;

		double[][][] y = new double[m][n][z];
		for (int j = 0; j < m; j++) {
			for (int i = 0; i < n; i++) {
				for (int t = 0; t < z; t++) {
					y[j][i][t] = a[j][i][t] * x;
				}
			}
		}
		return y;
	}

	public static double[] fillZero(double[] zero) {
		int l = zero.length;

		double[] temp = new double[l];
		for (int i = 0; i < l; l++) {
			temp[i] = 0;
		}

		return temp;
	}

	public static double[][] fillZero(double[][] zero) {
		int l = zero.length;
		int b = zero[0].length;
		double[][] temp = new double[l][b];
		for (int i = 0; i < l; l++) {
			for (int j = 0; j < b; j++) {
				temp[i][j] = 0;
			}
		}

		return temp;
	}

	public static double[][][] fillZero(double[][][] zero) {
		int l = zero.length;
		int b = zero[0].length;
		int n = zero[0][0].length;

		double[][][] temp = new double[l][b][n];

		for (int i = 0; i < l; l++) {
			for (int j = 0; j < b; j++) {
				for (int z = 0; z < n; z++) {
					temp[i][j][z] = (double) 0;
				}
			}
		}

		return temp;
	}

	public static double[][][][] fillZero(double[][][][] zero) {
		int l = zero.length;
		int b = zero[0].length;
		int n = zero[0][0].length;
		int m = zero[0][0][0].length;

		double[][][][] temp = new double[l][b][n][m];
		for (int i = 0; i < l; l++) {
			for (int j = 0; j < b; j++) {
				for (int z = 0; z < n; z++) {
					for (int t = 0; t < m; t++) {
						temp[i][j][z][t] = 0;
					}
				}
			}
		}

		return temp;
	}

	public static double[][] power(double[][] x, int a) {
		int m = x.length;
		int n = x[0].length;

		double[][] y = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				y[i][j] = Math.pow(x[i][j], a);
			}
		}
		return y;
	}

	public static double costMeanSq(double[][] desired, double[][] calculated) {
		double sum = 0;

		double[][] subt = new double[desired.length][desired[0].length];
		subt = Array.subtract(desired, calculated);

		int tempnum = 0;

		for (int i = 0; i < subt.length; i++) {
			for (int j = 0; j < subt[0].length; j++) {
				sum += Math.pow(subt[i][j], 2);
				tempnum++;
			}
		}
		return (sum / (desired.length * 2));
	}

	public static double[][] sigmoid(double[][] a) {
		int m = a.length;
		int n = a[0].length;
		double[][] z = new double[m][n];

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				z[i][j] = (1.0 / (1 + Math.exp(-a[i][j])));
			}
		}
		return z;
	}

	public static double sigmoid(double a) {
		return (1.0 / (1 + Math.exp(-a)));
	}

	public static double[][] derivativeSigmoid(double[][] actVal, String a) {
		int m = actVal.length;
		int n = actVal[0].length;
		double[][] temp = new double[m][n];

		temp = Array.multiply(actVal, Array.subtract(1, actVal));

		return temp;
	}

	public static double[] derivativeSigmoid(double[] sumVal) {
		int m = sumVal.length;

		double[] temp = new double[m];
		for (int i = 0; i < m; i++) {
			temp[i] = Array.sigmoid(sumVal[i]) * (1 - Array.sigmoid(sumVal[i]));
		}

		return temp;
	}

	public static double[] derivativeSigmoid(double[] actVal, String a) {
		int m = actVal.length;

		double[] temp = new double[m];

		for (int i = 0; i < m; i++) {
			temp[i] = actVal[i] * (1 - actVal[i]);
		}

		return temp;
	}

	public static double derivativeSigmoid(double sumVal) {
		double temp;

		temp = Array.sigmoid(sumVal) * (1 - Array.sigmoid(sumVal));

		return temp;
	}

	public static double[][] divide(double[][] x, int a) {
		int m = x.length;
		int n = x[0].length;

		double[][] z = new double[m][n];

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				z[i][j] = (x[i][j] / a);
			}
		}
		return z;
	}

	public static double cross_entropy(int batch_size, double[][] Y, double[][] A) {
		int m = A.length;
		int n = A[0].length;
		double[][] z = new double[m][n];

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {

				if (A[i][j] == 0) {
					A[i][j] = 1E-300;
				} // deðer tanýmsýz olacaðý için 0 olan deðerler yerine double limitinde küçük
					// deðerler koyuldu.
				z[i][j] = Y[i][j] * Math.log(A[i][j]) + (1 - Y[i][j]) * Math.log(1 - A[i][j]);
			}
		}

		double sum = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				sum += z[i][j];
			}
		}
		return (-sum / batch_size);
	}

	public static double[][] softmax(double[][] z) {

		double[][] zout = new double[z.length][z[0].length];
		double[] sum = new double[z.length];

		for (int i = 0; i < z.length; i++) {
			for (int j = 0; j < z[0].length; j++) {
				sum[i] += Math.exp(z[i][j]);
			}
		}

		for (int i = 0; i < z.length; i++) {
			for (int j = 0; j < z[0].length; j++) {
				zout[i][j] = Math.exp(z[i][j]) / sum[i];
			}
		}
		return zout;
	}

	public static double[] softmax(double[] z) {

		double[] zout = new double[z.length];
		double sum = 0;

		for (int i = 0; i < z.length; i++) {

			sum += Math.exp(z[i]);

		}

		for (int i = 0; i < z.length; i++) {

			zout[i] = Math.exp(z[i]) / sum;

		}
		return zout;
	}

	public static double[][][] derivativeSoftmax(int numHLNode, double[][] sum) {
		int batchSize = sum.length;
		int numOLNode = sum[0].length;

		double[][][] zout = new double[batchSize][numHLNode][numOLNode];

		double[][] temp1 = Array.multiply(Array.softmax(sum), Array.subtract(1, Array.softmax(sum)));

		for (int t = 0; t < batchSize; t++) {
			for (int i = 0; i < numHLNode; i++) {
				for (int j = 0; j < numOLNode; j++) {
					zout[t][i][j] = temp1[t][j];
				}
			}
		}
		return zout;
	}

	public static double[][] derivativeSoftmax(double[][] outActivatedValues) {

		double[][] temp1 = Array.multiply(outActivatedValues, Array.subtract(1, outActivatedValues));

		return temp1;
	}

	public static double[][] partialCopy(double[][] a, int start, int size) {
		double[][] b = new double[size][a[0].length];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < a[0].length; j++) {
				b[i][j] = a[i + start][j];
			}
		}
		return b;
	}

	public static void print(String val) {
		System.out.println(val);
	}
}