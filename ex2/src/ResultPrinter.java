import java.util.List;

import javax.swing.JFrame;

import org.math.plot.*;

public class ResultPrinter {

	public static void printResult(List<Double> result){
		double[] y = new double[result.size()];
		double[] x = new double[result.size()];
		for(int i = 0;i<result.size();++i){
			y[i] = result.get(i);
			x[i] = i;
		}
		// create your PlotPanel (you can use it as a JPanel)
		Plot2DPanel plot = new Plot2DPanel();
		// add a line plot to the PlotPanel
		plot.addLinePlot("my plot", x, y);
//		plot.
		// put the PlotPanel in a JFrame, as a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setContentPane(plot);
		frame.setVisible(true);
	}
}
