package solver.grammar;

import solver.MatrixUtil;

public class SolveRootProduction extends Production {

	public SolveRootProduction(Vertex vert) {
		super(vert);
	}

	@Override
	public void apply(Vertex vert) {
		System.out.println("root b:");
		MatrixUtil.printVector(vert.b);
		System.out.println();
		vert.x = MatrixUtil.gaussianElimination(vert.A, vert.b);
		
		System.out.println("root x: ");
		MatrixUtil.printVector(vert.x);
		System.out.println();
		
	}

}
