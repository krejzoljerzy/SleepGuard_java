package com.bb.sleepguard.src;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.sound.sampled.DataLine;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class SimpleConnector {

	static SerialPort serialPort;
	static ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	static int dataLength = 0;

	public SimpleConnector(String portName) {

		serialPort = new SerialPort(portName);
		try {
			serialPort.setParams(SerialPort.BAUDRATE_115200,
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);// Set params. Also you can set
											// params by this string:
											// serialPort.setParams(9600, 8, 1,
											// 0);

			int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS
					+ SerialPort.MASK_DSR;// Prepare mask
			serialPort.setEventsMask(mask);// Set mask
			serialPort.addEventListener(new SerialPortReader());// Add
																// SerialPortEventListener

		} catch (SerialPortException ex) {
			System.out.println(ex);
		}

	}

	public static String[] listAvailablePorts() {

		String[] portNames = SerialPortList.getPortNames();
		return portNames;

	}

	public void open() {

		try {
			serialPort.openPort();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void close() {

		try {
			serialPort.closePort();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void write(byte[] data) {
		try {
			serialPort.writeBytes(data);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(String data) {
		try {
			serialPort.writeBytes(data.getBytes());
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte[] read() {

		byte[] data = buffer.toByteArray();
		buffer.reset();
		dataLength = 0;
		return data;

	}

	static class SerialPortReader implements SerialPortEventListener {

		public void serialEvent(SerialPortEvent event) {
			if (event.isRXCHAR()) {// If data is available

				// Read data, if 10 bytes available
				try {
					int temp = event.getEventValue();
					buffer.write(serialPort.readBytes(temp));
					dataLength+=temp;
					
				} catch (SerialPortException | IOException ex) {
					System.out.println(ex);
				}

			} else if (event.isCTS()) {// If CTS line has changed state
				if (event.getEventValue() == 1) {// If line is ON
					System.out.println("CTS - ON");
				} else {
					System.out.println("CTS - OFF");
				}
			} else if (event.isDSR()) {// /If DSR line has changed state
				if (event.getEventValue() == 1) {// If line is ON
					System.out.println("DSR - ON");
				} else {
					System.out.println("DSR - OFF");
				}
			}
		}

	}
	
	public int peekLength(){
		return dataLength;
	}

}
