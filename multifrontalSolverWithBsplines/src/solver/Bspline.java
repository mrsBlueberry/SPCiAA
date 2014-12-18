package solver;

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
    private int findLastDOF(double x) {
        int i = 0;
        while (i < u.length - 1 && u[i] < x) {
            ++i;
        }
        return i - 1;
    }

}
