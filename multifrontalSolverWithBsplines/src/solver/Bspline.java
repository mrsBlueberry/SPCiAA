package solver;

import solver.grammar.Stuff;

public class Bspline {

	private double[] u;
	// b spline degree
	private int p;

	public Bspline(double[] knotVector, int degree) {
		this.u = knotVector;
		this.p = degree;
	}

	public double evaluate(double x, double[] parameters) {
        double[][] d = new double[p + 1][p + 1];
        // DOF - degree of fredoom!!!
        int lastDOF = findLastDOF(x);
        int firstDOF = lastDOF - p;
        for (int i = 0; i <= p; ++i) {
            int dof = firstDOF + i;
            if (dof >= 0 && dof < parameters.length) {
                d[0][i] = parameters[dof];
            }
        }
        for (int k = 1; k <= p; ++k) {
            for (int i = firstDOF + k; i <= lastDOF; ++i) {
                if (i >= 0 && i + p + 1 - k < u.length) {
                    double alpha = (x - u[i]) / (u[i + p + 1 - k] - u[i]);
                    int idx = i - firstDOF;
                    d[k][idx] = (1 - alpha) * d[k - 1][idx - 1] + alpha * d[k - 1][idx];
                }
            }
        }
        return d[p][p];
    }
	
	
	
	public double evaluate(double x, int n){
		double [] param = new double[Stuff.parametersCount];
		param[n] = 1;
		return evaluate(x, param);
		
	}
	
	public double evaluateDerivative(double x, double [] b){
		double [] b2 = new double[b.length];
		for(int i =0;i<b.length-1;++i){
			b2[i+1] = Stuff.p*(b[i+1]-b[i])/(Stuff.knotVector[i+Stuff.p+1]-Stuff.knotVector[i+1]);
		}
		Bspline bspline = new Bspline(Stuff.knotVector, Stuff.p-1);
		return bspline.evaluate(x, b2);
	}
	
	public double evaluateDerivative(double x , int n){
		double [] param = new double[Stuff.parametersCount];
		param[n]  = 1;
		return evaluateDerivative(x, param);
		
	}
    private int findLastDOF(double x) {
    	int i = 0;
    
    	try{
        
        while (i < u.length - 1 && u[i] < x) {
            ++i;
        }
    	}catch(Exception e){
        	e.printStackTrace();
        	return i-1;
        }
        return i - 1;
    }
    
    
   

}

