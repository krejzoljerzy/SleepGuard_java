package com.bb.sleepguard.src;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class JChartPanel extends JPanel {
	
	public JChartPanel(){
	
    super();
    final XYSeries series = new XYSeries("Random Data");
    series.add(1.0, 500.2);
    series.add(5.0, 694.1);
    series.add(4.0, 100.0);
    series.add(12.5, 734.4);
    series.add(17.3, 453.2);
    series.add(21.2, 500.2);
    series.add(21.9, null);
    series.add(25.6, 734.4);
    series.add(30.0, 453.2);
    final XYSeriesCollection data = new XYSeriesCollection(series);
    final JFreeChart chart = ChartFactory.createXYLineChart(
        "XY Series Demo",
        "X", 
        "Y", 
        data,
        PlotOrientation.VERTICAL,
        true,
        true,
        false
    );

    final ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(590, 505));
    add(chartPanel);
	}

}
