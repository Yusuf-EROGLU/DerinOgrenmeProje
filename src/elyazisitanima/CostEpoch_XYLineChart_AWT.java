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
		for (int i = 0; i < numberOfEpoch; i++) {
			if (i % sens == 0) {
				meanSquar.add(i, series1[i]);
			}
		}

		final XYSeries crossEntropy = new XYSeries("Cross Entropy");
		for (int i = 0; i < numberOfEpoch; i++) {
			if (i % sens == 0) {
				crossEntropy.add(i, series2[i]);
			}
		}

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(meanSquar);
		dataset.addSeries(crossEntropy);
		return dataset;
	}

}
