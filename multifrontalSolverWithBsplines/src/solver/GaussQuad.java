package solver;


public class GaussQuad {

    public static double[] points(int n) {
        checkOrder(n);
        return GaussQuadTable.POINTS[n];
    }
    
    public static double[] weights(int n) {
        checkOrder(n);
        return GaussQuadTable.WEIGHTS[n];
    }
    
    public static double eval(double[] vals) {
        int n = vals.length;
        checkOrder(n);
        
        double[] w = GaussQuadTable.WEIGHTS[n];
        double s = 0;
        
        for (int i = 0; i < n; ++ i) {
            s += vals[i] * w[i];
        }
        return s;
    }

    private static void checkOrder(int n) {
        if (n > GaussQuadTable.MAX_N) {
            String msg = String.format("Requested order %d, max is %d", n, 
                    GaussQuadTable.MAX_N);
            throw new IllegalArgumentException(msg);
        }
    }
    
}
