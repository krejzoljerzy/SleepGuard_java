package com.bb.sleepguard.src;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import java.awt.FlowLayout;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTree;
import javax.swing.JComboBox;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class SleepGuard extends JFrame {

	private JPanel contentPane;
	private static SimpleConnector connector;
	private static String[] portNames= SimpleConnector.listAvailablePorts();
	private static JChartPanel chartPanel;
	private static JComboBox<String> comboBox;
	private static ArrayList<DataContainer> record_list = new ArrayList<DataContainer>();
	private static JList list;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.alee.laf.WebLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SleepGuard frame = new SleepGuard();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SleepGuard() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(13, 12, 596, 537);
		contentPane.add(tabbedPane);
		
		chartPanel = new JChartPanel();
		tabbedPane.addTab("Chart", null, chartPanel, null);
		FlowLayout flowLayout = (FlowLayout) chartPanel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(621, 12, 151, 537);
		contentPane.add(tabbedPane_1);
		
		JPanel panel_1 = new JPanel();
		tabbedPane_1.addTab("Commands", null, panel_1, null);
		panel_1.setLayout(new MigLayout("", "[100px:150px:150px,center]", "[30px:35px:40px][30px:35px:40px][30px:35px:40px][30px:35px:40px][30px:35px:40px][30px:35px:40px][30px:35px:40px]"));
		
		JButton btnNewButton = new JButton("Read memory");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connector.write("r");
				DataWaiter waiter = new DataWaiter(chartPanel);
				waiter.start();

			}
		});
		panel_1.add(btnNewButton, "cell 0 0");
		
		JButton btnNewButton_1 = new JButton("Erase memory");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//connector.write("e");
			}
		});
		panel_1.add(btnNewButton_1, "cell 0 1");
		
		JButton btnNewButton_2 = new JButton("Start measurement");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connector.write("n");
			}
		});
		panel_1.add(btnNewButton_2, "cell 0 2");
		
		JButton btnNewButton_3 = new JButton("Stop measurement");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connector.write("s");
			}
		});
		panel_1.add(btnNewButton_3, "cell 0 3");
		list = new JList(new String[]{"No records - download from device"});
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				chartPanel.add(record_list.get(list.getSelectedIndex()).getData());
				
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(list);
		tabbedPane_1.addTab("Records", null, scrollPane, null);
		
		JPanel panel = new JPanel();
		tabbedPane_1.addTab("Connection", null, panel, null);
		
		comboBox = new JComboBox(portNames);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
			}
		});
		panel.setLayout(new MigLayout("", "[100px]", "[35px][35px]"));
		panel.add(comboBox, "cell 0 0,alignx left,aligny top");
		
		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connector = new SimpleConnector(portNames[comboBox.getSelectedIndex()]);
			}
		});
		panel.add(btnOpen, "cell 0 1");
		
		
		
	}
	
	public static void dataReady(){
		int i=0;
		DefaultListModel<String>listModel = new DefaultListModel<String>();
		byte[] data_bytes = connector.read();
		String data_str = new String(data_bytes);
		String[] data_str_records = data_str.split("Record_");
		data_str_records = Arrays.copyOfRange(data_str_records, 1, data_str_records.length);
		for (String str_record : data_str_records){
			byte[] temp_data_bytes = str_record.getBytes();
			byte[] number = Arrays.copyOfRange(temp_data_bytes, 1,5);
			byte[] samples = Arrays.copyOfRange(temp_data_bytes, 5,temp_data_bytes.length-1);
			DataContainer dc = new DataContainer(number, samples);
			listModel.addElement(dc.getName());
			record_list.add(dc);
		}
		list.removeAll();
		list.setModel(listModel);
	}
	
	private class DataWaiter extends Thread{
		
		int tempDataLength;
		JChartPanel chartPanel;
		
		public DataWaiter(JChartPanel chart){
			chartPanel = chart;
		}
		
		public void run() {
	        try {
	        	while(true){
					Thread.sleep(500);
					if(tempDataLength==connector.peekLength()){
						dataReady();
						return;
					}else {
						tempDataLength = connector.peekLength();
						System.out.println(tempDataLength);
					}
	        	}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    }
	}
	

}
