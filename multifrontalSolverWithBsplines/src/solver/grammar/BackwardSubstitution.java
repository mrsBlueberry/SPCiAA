package solver.grammar;

import java.util.Arrays;

import solver.MatrixUtil;

public class BackwardSubstitution extends Production {

	public BackwardSubstitution(Vertex vert) {
		super(vert);
	}

	@Override
	public void apply(Vertex vert) {

		int count = vert.type.equals(VertexType.LEAF_PARENT) ? Stuff.p + 1
				: Stuff.p * 2;
		int shift = vert.type.equals(VertexType.LEAF_PARENT) ? 1 : Stuff.p;
		for (int i = 0; i < vert.children.length; ++i) {
			Vertex child = vert.children[i];
			double[] x = Arrays.copyOfRange(vert.x, i * shift, i * shift + count);
			MatrixUtil.substitute(child.A, child.b, x, child.b.length - count);
			child.x = Arrays.copyOf(child.b, child.b.length);
			MatrixUtil.moveVector(child.x, child.b.length - count, Stuff.p);

		}

	}

}
