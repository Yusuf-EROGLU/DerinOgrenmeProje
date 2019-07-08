package elyazisitanima;

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.BasicStroke;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class CostEpoch_XYLineChart_AWT extends ApplicationFrame {

	public CostEpoch_XYLineChart_AWT(String applicationTitle, String chartTitle, int firstNetworkNumberOfEpoch,
			double[] firstNetworkMeanSquar, double[] firstNetworkCrossEntropy, int sensibility) {
		super(applicationTitle);

		XYDataset dataset = createDataset(firstNetworkNumberOfEpoch, sensibility, firstNetworkMeanSquar,
				firstNetworkCrossEntropy);

		JFreeChart xylineChart = ChartFactory.createXYLineChart(chartTitle, "Number of Epoch", "Cost Value", dataset,
				PlotOrientation.VERTICAL, true, true, true);
		ChartPanel chartPanel = new ChartPanel(xylineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1120, 367));
		final XYPlot plot = xylineChart.getXYPlot();

		setContentPane(chartPanel);

	}

	private XYDataset createDataset(int numberOfEpoch, int sens, double[] series1, double[] series2) {
		final XYSeries meanSquar = new XYSeries("Mean Square Cost");

		int size1 = series1.length / sens;
		int size2 = series2.length / sens;

		double[] tempseries1 = new double[size1];
		double[] tempseries2 = new double[size2];

		for (int i = 0; i < size1; i++) {
			double temp = 0;
			for (int j = i*sens; j < i*sens + sens; j++) {
				temp += series1[j];
			}
			tempseries1[i] = temp / sens;
		}
		
		

		for (int i = 0; i < size2; i++) {
			double temp = 0;
			for (int j = i*sens; j < i*sens + sens; j++) {
				temp += series2[j];
			}
			tempseries2[i] = temp / sens;
		}
		

		
		for (int i = 0; i < numberOfEpoch/sens; i++) {
			
				meanSquar.add((i+1)*sens, tempseries1[i]);
			
		}

		final XYSeries crossEntropy = new XYSeries("Cross Entropy");

		for (int i = 0; i < numberOfEpoch/sens; i++) {
			
				crossEntropy.add((i+1)*sens, tempseries2[i]);
		
		}

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(meanSquar);
		dataset.addSeries(crossEntropy);
		return dataset;
	}

}
