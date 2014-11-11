package com.bb.sleepguard.src;

import java.text.SimpleDateFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.SamplingXYLineRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

	
public class CopyOfJChartPanel extends JPanel {
	double data_backup[];
	
	static XYSeries series = new XYSeries("Sampled data");
    XYSeriesCollection data = new XYSeriesCollection(series);
    JFreeChart chart = ChartFactory.createXYLineChart(
        "Trace of breath in sleep",
        "Sample", 
        "mBar", 
        data,
        PlotOrientation.VERTICAL,
        true,
        true,
        false
    );
    ChartPanel chartPanel =new ChartPanel(chart);
	
	public CopyOfJChartPanel(){
    super();
    final XYDataset data1 = createDataset1();

    SamplingXYLineRenderer renderer1 = new SamplingXYLineRenderer();
    final DateAxis domainAxis = new DateAxis("Elapsed time");
    domainAxis.setAutoTickUnitSelection(true);
    domainAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss:SSS"));
    final ValueAxis rangeAxis = new NumberAxis("pressure [mBar]");
    
    final XYPlot plot = new XYPlot(data1, domainAxis, rangeAxis, renderer1);
    
   // final JFreeChart chart = new JFreeChart("Trace of breath pressure", plot);
    JFreeChart chart = ChartFactory.createXYLineChart(
    		"test",
    		"czas",
    		"cisnienie",
    		data1,
            PlotOrientation.VERTICAL,
            true,
            true,
            false);
    chart.getXYPlot().setDomainAxis(domainAxis);
    final ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(688, 510));
    add(chartPanel);
    
	}
	
    public XYDataset createDataset1() {

        final TimePeriodValues s1 = new TimePeriodValues("Supply");
        
            s1.add(new SimpleTimePeriod(0,20), 1);
            s1.add(new SimpleTimePeriod(21, 40), 2);
            s1.add(new SimpleTimePeriod(41, 60), 3);
            s1.add(new SimpleTimePeriod(981, 1000), 4);
            s1.add(new SimpleTimePeriod(1021, 1040), 5);
        

        final TimePeriodValuesCollection dataset = new TimePeriodValuesCollection();
        dataset.addSeries(s1);

        return dataset;

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
