package com.bb.sleepguard.src;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class DataContainer {
	
	private byte[] data_local;
	private String name = "";


	public DataContainer(byte[] number, byte[] samples) {
		name = "Record_";
		ByteBuffer bb = ByteBuffer.wrap(number);
		bb.order(ByteOrder.BIG_ENDIAN);
		name +=Integer.toString(bb.getInt());
		data_local = samples;
	}
	
	public double[] getData(){
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
	
	public String getName(){
		return name;
	}

}
