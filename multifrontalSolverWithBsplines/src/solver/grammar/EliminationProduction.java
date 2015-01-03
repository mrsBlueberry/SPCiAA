package solver.grammar;

import solver.MatrixUtil;

public class EliminationProduction extends Production {

	public EliminationProduction(Vertex vert) {
		super(vert);
	}

	@Override
	public void apply(Vertex vert) {

		int count = vert.type.equals(VertexType.LEAF_PARENT) ? 1 : Stuff.p;
		MatrixUtil.moveColumns(vert.A, Stuff.p, count);
		MatrixUtil.moveRows(vert.A, Stuff.p, count);
		MatrixUtil.moveVector(vert.b, Stuff.p, count);
		MatrixUtil.partiallyEliminate(vert.A, vert.b, count);
	}

}
