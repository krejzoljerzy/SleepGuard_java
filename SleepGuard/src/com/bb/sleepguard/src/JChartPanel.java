package com.bb.sleepguard.src;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.util.RelativeDateFormat;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class JChartPanel extends JPanel {
	
	double data_backup[];
	
	static XYSeries series = new XYSeries("Sampled data");
    XYSeriesCollection data = new XYSeriesCollection(series);
    JFreeChart chart = ChartFactory.createXYLineChart(
        "Trace of breath in sleep",
        "elapsed time", 
        "pressure [mBar]", 
        data,
        PlotOrientation.VERTICAL,
        true,
        true,
        false
    );
    final DateAxis domainAxis = new DateAxis("elapsed time");

	
	public JChartPanel(){
    super();
    domainAxis.setAutoTickUnitSelection(true);
    RelativeDateFormat rdf = new RelativeDateFormat(0);
    rdf.setHourSuffix( "h ");
    rdf.setMinuteSuffix("m ");
    rdf.setSecondSuffix("s ");
    domainAxis.setDateFormatOverride(rdf);
    chart.getXYPlot().setDomainAxis(domainAxis);
    ChartPanel chartPanel =new ChartPanel(chart);
    chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(688, 510));
    add(chartPanel);
	}
	
	public void add(double[]y){
		data_backup = y;
		series.clear();
		Printer printer = new Printer();
		printer.start();
		int length =0;
		
		for(double data : y){
			
			series.add((length++)*20,data,false);
		}
		series.add((length++)*20,0.0);
		
		int i=0;
		i++;
		
	}
	
	public void add(double[]y,int begining){
		data_backup = y;
		series.clear();
		Printer printer = new Printer();
		printer.start();
		int length = begining;
		
		for(double data : y){
			
			series.add((length++)*20,data,false);
		}
		series.add((length++)*20,0.0);
		
		int i=0;
		i++;
		
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
