package solver.grammar;

import solver.MatrixUtil;

public class Vertex {
	
	public VertexType type;
	public Vertex parent;
	public Vertex[] children;
	public double [][] A;
	public double [] b;
	public double [] x;
	public double [] xPrev;
	public Element element;
	
	public Vertex(Vertex parent){
		this.parent = parent;
	}
	
	public void initializeVertex(int matrixSize, VertexType type,
			int childrenCount) {
		this.A = new double[matrixSize][matrixSize];
		this.type = type;
		this.b = new double[matrixSize];
		this.x = new double[matrixSize];
		this.xPrev = new double[matrixSize];

		this.children = childrenCount>0?new Vertex[childrenCount]:new Vertex[0];
		for (int i = 0; i < childrenCount; ++i) {
			this.children[i] = new Vertex(this);
		}
		System.out.println("initialized type: "+type);
	
	}
	
	public void generateRandomValues(){
		A = MatrixUtil.generateRandomMatrix(A.length, A.length);
		b = MatrixUtil.generateRandomVector(b.length);
	}

}




class Element{
	

	public double x0;
	public double x1;
	public int left;
	public int right;
	
	public Element(double x0, double x1, int left, int right) {
		
		this.x0 = x0;
		this.x1 = x1;
		this.left = left;
		this.right = right;
	}
	
	
	
}