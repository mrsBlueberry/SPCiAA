package solver;

import java.util.Arrays;

import solver.grammar.Stuff;



public class Main {

	public static void main(String[] args) {
//		double [][] matrix = MatrixUtil.generateRandomMatrix(6, 6);
//		MatrixUtil.printMatrix(matrix);
//		System.out.println();
//		MatrixUtil.moveColumns(matrix, 2, 3);
//		MatrixUtil.printMatrix(matrix);
		double [] vector = MatrixUtil.generateRandomVector(5);
		MatrixUtil.printVector(vector);
		System.out.println();
		MatrixUtil.moveVector(vector, 2, 2);
		MatrixUtil.printVector(vector);
		
		Bspline spline = new Bspline(Stuff.knotVector, Stuff.p);
		double [] parameters = new double[Stuff.parametersCount];
		parameters[0]  =1;
//		parameters[3] = 1;
//		parameters[7] = 1;
		int size = 1000;
		double [] x = new double[size];
		double[] y = new double[size];
		double[] dy = new double[size];
		double[] dy2 = new double[size];
	
		double cus = 0.0;
		for(int i=0;i<size;++i){
			x[i] = cus;
			cus += 1.0/size;
			y[i] = spline.evaluate(x[i], parameters);
			
			
		}
	
		
		
		for(int i=0;i<size-1;++i){
			dy[i] = (y[i + 1] - y[i]) / (x[i + 1] - x[i]);
			dy2[i] = spline.evaluateDerivative(x[i], parameters);
		}
		
		ResultPrinter.printResult(x, y);
//		ResultPrinter.printResult(Arrays.copyOfRange(x, 0, size-1), dy);
//		ResultPrinter.printResult(x, dy2);
		
		

	}

}
