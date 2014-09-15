package com.bb.sleepguard.src;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class JChartPanel extends JPanel {
	
	static XYSeries series = new XYSeries("Sampled data");
    XYSeriesCollection data = new XYSeriesCollection(series);
    JFreeChart chart = ChartFactory.createXYLineChart(
        "XY Series Demo",
        "X", 
        "Y", 
        data,
        PlotOrientation.VERTICAL,
        true,
        true,
        false
    );
    ChartPanel chartPanel =new ChartPanel(chart);
	
	public JChartPanel(){
	
    super();
    series = new XYSeries("Random Data");
    data = new XYSeriesCollection(series);
    chart = ChartFactory.createXYLineChart(
        "XY Series Demo",
        "X", 
        "Y", 
        data,
        PlotOrientation.VERTICAL,
        true,
        true,
        false
    );

    chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(590, 505));
    add(chartPanel);
	}
	
	public void add(int[]y){
		int length = series.getItemCount();
		
		length++;
		for(int data : y){
			series.add(length++, data/1000.0);
		}
		
		
		
	}

}
