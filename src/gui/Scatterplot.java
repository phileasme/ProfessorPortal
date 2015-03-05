package gui;

import java.awt.Dimension;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class Scatterplot extends ApplicationFrame {

	

	XYSeries dataSeries;
	
	public Scatterplot(String assName) {
		super(assName + " vs. Average");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		dataSeries = new XYSeries("marks"); 
	}
	
	public void addPoint(double avgMark, double mark) {
		dataSeries.add(avgMark, mark);
	}
	
	public void launch() {
		XYSeriesCollection data = new XYSeriesCollection(dataSeries);
		
		JFreeChart chart = ChartFactory.createScatterPlot("ScatterPlot", "Class Average", "Individual Mark", data, PlotOrientation.VERTICAL, false, false, false);
		ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new Dimension(600, 400));
		
		setContentPane(panel);
		
		pack();
		setVisible(true);
	}
}
