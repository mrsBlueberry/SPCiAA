package solver;

import java.util.Arrays;
import java.util.Random;

public class MatrixUtil {

	public static void printMatrix(double[][] matrix) {

		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < matrix[0].length; ++j) {
				System.out.print(String.format("%5.2f", matrix[i][j]));
			}
			System.out.println();
		}
	}

	public static void printVector(double[] vector) {
		for (int i = 0; i < vector.length; ++i) {
			System.out.print(String.format("%5.2f", vector[i])+"   ");
		}
	}
	
	public static double[] multiplyMatrixVectorLeft(double[][] matrix, double[] vector) {
		if (matrix[0].length != vector.length) {
			throw new IllegalArgumentException();
		}
		double[] result = new double[matrix.length];
		for (int i = 0; i < matrix.length; ++i) {
			for (int j = 0; j < matrix[0].length; ++j) {
				result[i] += vector[j] * matrix[i][j];
			}
		}

		return result;
	}

	public static double[][] generateRandomMatrix(int size1, int size2) {
		double[][] matrix = new double[size1][size2];
		Random rand = new Random();
		for (int i = 0; i < size1; ++i) {
			for (int j = 0; j < size2; ++j) {
				matrix[i][j] = rand.nextDouble();
			}
		}

		return matrix;

	}

	public static double[] generateRandomVector(int size) {
		double[] vector = new double[size];
		Random rand = new Random();
		for (int i = 0; i < size; ++i) {
			vector[i] = rand.nextDouble();
		}
		return vector;
	}

		
	public static double[] gaussianElimination(double[][] A, double[] x) {
		if (A.length != A[0].length || A.length != x.length) {
			throw new IllegalArgumentException("Incompatible dimensions");
		}
		int N = x.length;

		double[] b = Arrays.copyOf(x, x.length);

		for (int i = 0; i < N; ++i) {
			int c = maxAbsVal(A, i);
			swap(A, i, c);
			swap(b, i, c);
			double v = A[i][i];
			for (int j = i; j < N; ++j) {
				A[i][j] /= v;
			}
			b[i] /= v;
			for (int j = i + 1; j < N; ++j) {
				double f = A[j][i];
				for (int k = i; k < N; ++k) {
					A[j][k] -= f * A[i][k];
				}
				b[j] -= f * b[i];
			}
		}

		for (int i = N - 1; i > 0; --i) {
			for (int j = i - 1; j >= 0; --j) {
				b[j] -= b[i] * A[j][i];
				A[j][i] = 0;
			}
		}

		return b;
	}
	
	public static void partiallyEliminate(double[][] A, double[] b, int p) {
        int N = A.length;
        for (int i = 0; i < p; ++ i) {
            double v = A[i][i];
            for (int j = i; j < N; ++ j) {
                A[i][j] /= v;
            }
            b[i] /= v;
            for (int j = i + 1; j < N; ++ j) {
                double x = A[j][i];
                for (int k = i; k < N; ++ k) {
                    A[j][k] -= x * A[i][k];
                }
                b[j] -= x * b[i];
            }
        }
        for (int i = p - 1; i > 0; -- i) {
            for (int j = i - 1; j >= 0; -- j) {
                double v = A[j][i];
                for (int k = i; k < N; ++ k) {
                    A[j][k] -= v * A[i][k];
                }
                b[j] -= v * b[i];
            }
        }
    }
	
	public static void moveRows(double[][] matrix, int rowNum, int rowCount) {

		if (rowNum + rowCount - 1 > matrix.length) {
			throw new IllegalArgumentException();
		}

		double[][] tmp = new double[rowCount][matrix[0].length];
		for (int i = 0; i < rowCount; ++i) {
			tmp[i] = matrix[rowNum + i];
		}
		for (int i = rowNum + rowCount - 1; i >= rowCount; --i) {
			matrix[i] = matrix[i - rowCount];
		}

		for (int i = 0; i < rowCount; ++i) {
			matrix[i] = tmp[i];
		}

	}

	public static void moveColumns(double[][] matrix, int colNum, int colCount) {

		double[][] tmp = new double[matrix.length][colCount];
		for (int i = 0; i < colCount; ++i) {

			for (int j = 0; j < matrix.length; ++j) {
				tmp[j][i] = matrix[j][colNum + i];
			}
		}

		for (int i = colNum + colCount - 1; i >= colCount; --i) {
			for (int j = 0; j < matrix.length; ++j) {
				matrix[j][i] = matrix[j][i - colCount];
			}

		}

		for (int i = 0; i < colCount; ++i) {
			for (int j = 0; j < matrix.length; ++j) {
				matrix[j][i] = tmp[j][i];
			}
		}

	}

	public static void moveVector(double[] vector, int num, int count) {
		double[] tmp = Arrays.copyOfRange(vector, num, num + count);
		for (int i = num + count - 1; i >= count; --i) {
			vector[i] = vector[i - count];
		}
		for (int i = 0; i < tmp.length; ++i) {
			vector[i] = tmp[i];
		}
	}
	
	public static void substitute(double[][] A, double[] b, double[] x, int p) {
        int N = A.length;
        for (int i = 0; i < p; ++ i) {
            for (int j = p; j < N; ++ j) {
                b[i] -= A[i][j] * x[j - p];
            }
        }
        for (int i = p; i < N; ++ i) {
            b[i] = x[i - p];
        }
    }
	
	public static void glueMatrixes(double[][][] matrices, int interval, int omitCount, double [][] result){
		for(int k=0;k<matrices.length;++k){
			for(int i=omitCount;i<matrices[0].length;++i){
				for(int j=omitCount;j<matrices[0].length;++j){
					result[i+k*interval-omitCount][j+k*interval-omitCount] += matrices[k][i][j];
				}
			}
		}
	}

	public static void glueVectors(double[][] v, int interval, int omitCount, double [] result) {
		int k = v.length;
		int N = v[0].length;
		for (int i = 0; i < k; ++i) {
			for (int j = omitCount; j < N; ++j)
				result[j + i*interval-omitCount] += v[i][j];
		}
	}

	
	private static void swap(double [][] x, int i, int j) {
		double [] tmp = x[i];
		x[i] = x[j];
		x[j] = tmp;
	}

	private static void swap(double[] x, int i, int j) {
		double tmp = x[i];
		x[i] = x[j];
		x[j] = tmp;
	}

	private static int maxAbsVal(double[][] A, int col) {
		int c = col;
		for (int i = col + 1; i < A.length; ++i) {
			if (Math.abs(A[i][col]) > Math.abs(A[c][col])) {
				c = i;
			}
		}
		return c;
	}

}
