package solver.grammar;

import java.util.Arrays;

import solver.MatrixUtil;

public class CombineChildrenProducion extends Production{

	int interval;
	int ommitCount;
	public CombineChildrenProducion(Vertex vert, int interval, int ommitCount) {
		super(vert);
		this.interval = interval;
		this.ommitCount = ommitCount;
	}

	@Override
	public void apply(Vertex vert) {
		int childrenCount = vert.children.length;
		double [][][] children = new double[childrenCount][][];
		double [][] childrenVectors = new double[childrenCount][];
		for(int i=0;i<childrenCount;++i){
			children[i] = vert.children[i].A;
			childrenVectors[i] = vert.children[i].b;
		}
		Arrays.fill(vert.b, 0);
		for(double [] a: vert.A){
			Arrays.fill(a, 0);
		}
		MatrixUtil.glueMatrixes(children, interval, ommitCount, vert.A);
		MatrixUtil.glueVectors(childrenVectors, interval, ommitCount, vert.b);
	}

}
