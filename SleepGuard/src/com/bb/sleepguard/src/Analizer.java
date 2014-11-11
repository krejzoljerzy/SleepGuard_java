package com.bb.sleepguard.src;

import java.util.ArrayList;
import java.util.Arrays;


public class Analizer {
	
	private final int LOOKING_FOR_START=0;
	private final int LOOKING_FOR_END=1;
	private final int FOUND=2;
	private int state = LOOKING_FOR_START;
	private int current_length = 0;
	private int start_index = 0;
	private int end_index = 0;
	private final int freq = 50;
	private final int padding = 1/*seconds*/*freq;
	
	public Analizer(){
		
	}
	
	public ArrayList<DataContainer> startAnalizing(DataContainer data, double pressure, double time){
		ArrayList<DataContainer> cropped_data = new ArrayList<DataContainer>();
		int sample_treshold = (int)(time*freq);
		double[] data_double_no_mean = data.getData(DataContainer.NO_MEAN);
		double[] data_double_raw = data.getData(DataContainer.RAW);
		double[] data_double = data_double_no_mean.clone();
		for(int i=0;i<data_double.length;i++){
			data_double[i] = Math.abs(data_double[i]);
		}
		
		/* Start looking for apnea symtpoms */
		for (int i=0;i<data_double.length;i++){

			switch(state)
			{
			case LOOKING_FOR_START:
				if(data_double[i]<=pressure){
					state = LOOKING_FOR_END;
					current_length=0;
					start_index=i;		
					
				}
				break;
			case LOOKING_FOR_END:
				
				if(data_double[i]<=pressure){
					current_length++;
					
				}
				else
				{
					if (current_length >= sample_treshold){
						state = FOUND;
						end_index=i;
					}
					else
					{
						state = LOOKING_FOR_START;
					}
				}
				
				break;

			}
			
			if(state == FOUND){
				state = LOOKING_FOR_START;
				
				int low_padding = start_index - padding;
				if(low_padding<0){
					low_padding = 0;
				}
				int high_padding = end_index + padding;
				if(high_padding>= data_double.length){
					high_padding = data_double.length-1;
				}
				
				//double[] temp_data = Arrays.copyOfRange(data_double_raw, low_padding, high_padding);
				double[] temp_data = Arrays.copyOfRange(data_double_no_mean, start_index, end_index);
				
				cropped_data.add(new DataContainer("Part "+Integer.toString(cropped_data.size()+1), temp_data,start_index));
				end_index = 0;
				start_index = 0;
			}
			
			
			
		}		
		return  cropped_data;
	}

}
