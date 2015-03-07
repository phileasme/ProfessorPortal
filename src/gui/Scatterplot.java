package gui;

import java.awt.Dimension;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

/**
 * Creates a scatterplot using students' results on assessments. Has methods
 * for adding data points to the series and launching the graph in a new window.
 * The graph area can be zoomed in by dragging the mouse left-to-right, and
 * zoomed out by right-to-left.
 * <p>
 * The final graph should have points for each row (i.e. each student) in the 
 * currently selected table in the main application window. The coordinates
 * should be the student's mark for the current assessment (Y-axis) against the
 * average mark achieved across all other assessments (X-axis).
 * 
 * @author Phileas Hocquard
 * @author Max Karasinski
 *
 */
public class Scatterplot extends ApplicationFrame {

	private XYSeries dataSeries;
	
	/**
	 * Creates a frame for the scatterplot and sets up an empty data series.
	 * 
	 * @param assName the name of the currently selected assessment
	 */
	public Scatterplot(String assName) {
		super(assName + " vs. Average");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		dataSeries = new XYSeries("marks"); 
	}
	
	/**
	 * Adds a new data point to the series.
	 * 
	 * @param avgMark the average mark across all other assessments
	 * @param mark the mark for the current assessment
	 */
	public void addPoint(double avgMark, double mark) {
		dataSeries.add(avgMark, mark);
	}
	
	/**
	 * Creates the graphical elements of the graph and makes it visible.
	 */
	public void launch() {
		XYSeriesCollection data = new XYSeriesCollection(dataSeries);
		
		JFreeChart chart = ChartFactory.createScatterPlot(null, "Average Marks", "Assessment Mark", data, PlotOrientation.VERTICAL, false, false, false);
		ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new Dimension(600, 400));
		
		setContentPane(panel);
		
		pack();
		setVisible(true);
	}
}
