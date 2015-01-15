package solver.grammar;

import java.util.Arrays;

import solver.Bspline;
import solver.GaussQuad;
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
	public int start;
	public int end;
	
	public Vertex(Vertex parent, int st, int end){
		this.parent = parent;
		this.start = st;
		this.end = end;
		System.out.println("I'm the number " + start + " to " + end +" motherfucker!!");
		
	}
	
	public void initializeVertex(int matrixSize, VertexType type,
			int childrenCount) {
		this.A = new double[matrixSize][matrixSize];
		this.type = type;
		this.b = new double[matrixSize];
		this.x = new double[matrixSize];
		this.xPrev = new double[matrixSize];

		int parentCount = end-start;
		int step = childrenCount>2?1:parentCount/2;
		int nextRange = start;
		this.children = childrenCount>0?new Vertex[childrenCount]:new Vertex[0];
		for (int i = 0; i < childrenCount; ++i) {
			this.children[i] = new Vertex(this, nextRange, nextRange+ parentCount/childrenCount );
			nextRange += step;
		}
	
	}
	
	public void generateRandomValues(){
		A = MatrixUtil.generateRandomMatrix(A.length, A.length);
		b = MatrixUtil.generateRandomVector(b.length);
	}
	
	public void generateMatrix(){
		double[] points = GaussQuad.points(Stuff.p + 1);
		double[] weights = GaussQuad.weights(Stuff.p + 1);
		double intervalSt = Stuff.knotVector[Stuff.p  + start];
		double intervalEnd = Stuff.knotVector[Stuff.p  + start + 1];
		
		xPrev = Arrays.copyOfRange(x, 0, x.length);

		Arrays.fill(b, 0);
		for(int i =0;i<A.length;++i){
			Arrays.fill(A[i], 0);
		}

		for(int i=0;i<points.length;++i){
			double t = (points[i]+1)/2;
			double x = t*intervalEnd + (1-t)*intervalSt;
			for(int j=start;j<start+Stuff.p+1;++j){
				Bspline spline = new Bspline(Stuff.knotVector, Stuff.p);
				double cus = spline.evaluate(x, j);
				double evalD = spline.evaluateDerivative(x, j);
				for(int k=start;k<start+Stuff.p+1;++k){
					double cus2 = spline.evaluate(x, k);
					double evalD2 = spline.evaluateDerivative(x, k);
					A[j-start][k-start] += cus*cus2*0.5*(intervalEnd-intervalSt)*weights[i];
					b[j - start] -= Stuff.dt*(evalD * evalD2 * xPrev[k - start] * 0.5
							* (intervalEnd - intervalSt) * weights[i]
							+ spline.evaluate(1, j) - spline.evaluate(0, j));
					if(j==0){
						b[j - start] += 1*Stuff.dt;
					}
				}
//				b[j-start] += cus*0.5*(intervalEnd-intervalSt)*weights[i]*Stuff.f(x);
				
			}
		}
		
		MatrixUtil.addToVecotr(b,MatrixUtil.multiplyMatrixVectorLeft(A, xPrev));	
		
		
		
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