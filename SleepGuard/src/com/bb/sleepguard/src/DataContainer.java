package com.bb.sleepguard.src;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.analysis.integration.*;

public class DataContainer {
	
	private byte[] data_local;
	private double[] data_double;
	private String name = "";
	public static final int INTEGRATED = 1;
	public static final int NO_MEAN = 2;
	public static final int RAW = 0;
	public static final int INTEGRATED_AND_NO_MEAN = 3;
	private int beginning =0;
	


	public DataContainer(byte[] number, byte[] samples) {
		name = "Record_";
		ByteBuffer bb = ByteBuffer.wrap(number);
		bb.order(ByteOrder.BIG_ENDIAN);
		name +=Integer.toString(bb.getInt());
		data_local = samples;
		data_double = convert();
	}

	public DataContainer(String record_name, byte[] samples) {
		name = record_name;
		data_local = samples;
		data_double = convert();
	}
	
	public DataContainer(String record_name, double[] samples) {
		name = record_name;
		data_double = samples;
		data_local = convertFromDouble(samples);
		
	}
	
	public DataContainer(String record_name, double[] samples, int sample_number) {
		name = record_name;
		data_double = samples;
		data_local = convertFromDouble(samples);
		beginning = sample_number;
		
	}
	
	public void setBeginning (int sample_number){
		beginning = sample_number;
	} 
	
	public int getBeginning (){
		return beginning;
	} 

	
	private double[] convert(){
		double [] data = new double[data_local.length/4];
		int data_ptr=0;
		for(int i =0;i<data_local.length;i+=4){
			if(i+4<data_local.length){
				byte[] temp_array = Arrays.copyOfRange(data_local, i, i+4);
				ByteBuffer bb = ByteBuffer.wrap(temp_array);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				data[data_ptr++]=(double)bb.getInt()/1000.0;
			}
			
		}
		return data;
	}
	
	private byte[] convertFromDouble(double[] data){
		byte[] data_out= new byte[data.length*4];
		int[] data_int= new int[data.length];
		for (int i=0;i<data.length;i++){
			data_int[i] = (int)(data[i]*1000);
			int replacer =0;
			for(int k=i*4;k<(i+1)*4;k++){
				data_out[k]=(byte)((data_int[i]>>replacer++)&0xFF);
			}
			
		}
		

		//output in hex would be 40,50,5b,85,1e,b8,51,ec
		return data_out;
	}
	
	public double[] getData(){

		return data_double.clone();
	}
	
	public double[] getData (int type){
		double[] data_ret = data_double.clone();
		switch(type){
		case RAW:
			break;
		case INTEGRATED:
			data_ret = integrate(data_ret);
			break;
		case NO_MEAN:
			data_ret = removeMean(data_ret);
			break;
		case INTEGRATED_AND_NO_MEAN:
			data_ret = removeMean(data_ret);
			data_ret = integrate(data_ret);
			break;
			
			
		}
		return data_ret;
	}
	
	
@Deprecated
	public byte[] getDataBytes(){
		return data_local;
	}
	
	public String getName(){
		return name;
	}
	
	public void clear(){
		data_local = null;
		data_double = null;
	}
	
	public DataContainer clone()
	{
		String name_temp = new String(name);
		double[] data_temp = data_double.clone();
		return new DataContainer(name_temp,data_temp);
	}

	private double[] removeMean(double[] data){
		Mean mean = new Mean();
		double mean_value = mean.evaluate(data);
		for(int i=0; i<data.length;i++)
		{
			data[i] -=mean_value;
		}
		return data;
	}
	
	private double[] integrate(double[] data){
		double[] integrated_data = new double[data.length];
		/* First step*/
		integrated_data[0] = data[0];
		for(int i=1;i<data.length;i++){
			integrated_data[i] = data[i]+ integrated_data[i-1];
		}
		return integrated_data;
	}
}
