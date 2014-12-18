package solver;



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
		
		double [] knotVector = {0, 0.2, 0.4, 0.6, 0.8, 1};
		Bspline spline = new Bspline(knotVector, 3);
		double [] parameters = {0, 0, 1, 0, 0};
		int size = 1000;
		double [] x = new double[size];
		double[] result = new double[size];
	
		double cus = 0.0;
		for(int i=0;i<size;++i){
			x[i] = cus;
			cus += 1.0/size;
			result[i] = spline.evaluate(x[i], parameters);
			
		}
		
		ResultPrinter.printResult(x, result);
		
		

	}

}
