package com.bb.sleepguard.src;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class JChartPanel extends JPanel {
	
	double data_backup[];
	
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
	
	public void add(double[]y){
		data_backup = y;
		series.clear();
		int length = series.getItemCount();
		Printer printer = new Printer();
		printer.start();
		length++;
		for(double data : y){
			series.add(length++, data,false);
		}
		series.add(length++,0.0);
		
		int i=0;
		i++;
		
	}
	
	public void printChart(int[]y){
		
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
	    int length = 0;
		for(int data : y){
			series.add(length++, data/1000.0);
		}
	    chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(590, 505));
	    add(chartPanel);
	    this.revalidate();
		
		
		
	}
	
	private class Printer extends Thread{
		
		public void run(){
			while(true){
				try {
					System.out.println(series.getItemCount() +"/"+data_backup.length);
					Thread.sleep(250);
					if(series.getItemCount()>=data_backup.length){
						return;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		}
		
	}

}
