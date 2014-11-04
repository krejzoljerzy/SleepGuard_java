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
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTree;
import javax.swing.JComboBox;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SleepGuard extends JFrame {

	private JPanel contentPane;
	private static SimpleConnector connector;
	private static String[] portNames = SimpleConnector.listAvailablePorts();
	private static JChartPanel chartPanel;
	private static JComboBox<String> comboBox;
	private static ArrayList<DataContainer> record_list = new ArrayList<DataContainer>();
	private static JList list;
	private static JFileChooser chooser;
	private static String choosertitle;
	private SleepGuard handle;

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
		handle = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1011, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(13, 12, 695, 537);
		contentPane.add(tabbedPane);

		chartPanel = new JChartPanel();
		tabbedPane.addTab("Chart", null, chartPanel, null);
		FlowLayout flowLayout = (FlowLayout) chartPanel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(720, 12, 263, 537);
		contentPane.add(tabbedPane_1);

		JPanel panel_1 = new JPanel();
		tabbedPane_1.addTab("Commands", null, panel_1, null);
		panel_1.setLayout(new MigLayout("", "[280px,center]", "[30px:35px:40px][30px:35px:40px][30px:35px:40px][30px:35px:40px]"));

		JButton btnNewButton = new JButton("Read memory");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connector.write("r");
				DataWaiter waiter = new DataWaiter(chartPanel);
				waiter.start();

			}
		});
		panel_1.add(btnNewButton, "cell 0 0,grow");

		JButton btnNewButton_1 = new JButton("Erase memory");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connector.write("e");
			}
		});
		panel_1.add(btnNewButton_1, "cell 0 1,grow");

		JButton btnNewButton_2 = new JButton("Start measurement");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connector.write("n");
			}
		});
		panel_1.add(btnNewButton_2, "cell 0 2,grow");

		JButton btnNewButton_3 = new JButton("Stop measurement");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connector.write("s");
			}
		});
		panel_1.add(btnNewButton_3, "cell 0 3,grow");

		JPanel panel_3 = new JPanel();
		tabbedPane_1.addTab("Records", null, panel_3, null);
		list = new JList(new String[] { "No records - download from device" });
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				chartPanel.add(record_list.get(list.getSelectedIndex())
						.getData());

			}
		});
		panel_3.setLayout(new MigLayout("", "[120px,grow]", "[500px][100px,grow]"));

		JScrollPane scrollPane = new JScrollPane(list);
		panel_3.add(scrollPane, "cell 0 0,grow");

		JPanel panel_2 = new JPanel();
		panel_3.add(panel_2, "cell 0 1,grow");
		panel_2.setLayout(new MigLayout("", "[150px][150px]", "[32px]"));

		JButton btnNewButton_4 = new JButton("Save records");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Save records");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				//
				// disable the "All files" option.
				//
				chooser.setAcceptAllFileFilterUsed(false);
				//
				if (chooser.showSaveDialog(handle) == JFileChooser.APPROVE_OPTION) {
					System.out.println("getCurrentDirectory(): "
							+ chooser.getCurrentDirectory());
					Calendar calendar = Calendar.getInstance();
					String folder = String.format("%02d",(calendar.get(Calendar.HOUR_OF_DAY)))+".";
					folder+=String.format("%02d",(calendar.get(Calendar.MINUTE)))+"-";
					folder+=String.format("%02d",(calendar.get(Calendar.DAY_OF_MONTH)))+"-";
					folder+=String.format("%02d",(calendar.get(Calendar.MONTH)))+"-";
					folder+=Integer.toString(calendar.get(Calendar.YEAR));
					File file = new File(chooser.getSelectedFile().getAbsolutePath()+"-"+folder);
					String path = file.getAbsolutePath();
					if(file.exists()){
						return;
					}
					file.mkdirs();
					if (record_list.size()>0){
						for(DataContainer d : record_list){
							file = new File(path+"/"+d.getName()+".slg");
							try {
								FileOutputStream fos = new FileOutputStream(file);
								fos.write(d.getDataBytes());
								fos.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					
				} else {
					System.out.println("No Selection ");
				}
				
			}
		});
		panel_2.add(btnNewButton_4, "cell 0 0,grow");
		
		JButton btnLoadRecords = new JButton("Load records");
		btnLoadRecords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle(choosertitle);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileFilter filter = new FileNameExtensionFilter("SleepGuard files","slg");
				chooser.addChoosableFileFilter(filter);
				chooser.setMultiSelectionEnabled(true);
				//
				// disable the "All files" option.
				//

				//
				if (chooser.showOpenDialog(handle) == JFileChooser.APPROVE_OPTION) {

					File[] files = chooser.getSelectedFiles();
					if(files!=null){
						if(files.length>0){
							record_list.clear();
							FileInputStream fis;
							for(File f : files){
								try {
									fis = new FileInputStream(f);
									byte[] data = new byte[(int) f.length()];
									fis.read(data);
									record_list.add(new DataContainer(f.getName().substring(0,f.getName().length()-4), data));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}								
							}
							DefaultListModel<String> listModel = new DefaultListModel<String>();
							for(DataContainer d : record_list){
								listModel.addElement(d.getName());	
							}
							list.removeAll();
							list.setModel(listModel);
						}
					}
					
					
				} else {
					System.out.println("No Selection ");
				}
				
			}
		});
		panel_2.add(btnLoadRecords, "cell 1 0,grow");

		JPanel panel = new JPanel();
		tabbedPane_1.addTab("Connection", null, panel, null);

		comboBox = new JComboBox(portNames);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
			}
		});
		panel.setLayout(new MigLayout("", "[250px]", "[35px][35px]"));
		panel.add(comboBox, "cell 0 0,grow");

		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connector = new SimpleConnector(portNames[comboBox
						.getSelectedIndex()]);
			}
		});
		panel.add(btnOpen, "cell 0 1,grow");

	}

	public static void dataReady() {
		int i = 0;
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		byte[] data_bytes = connector.read();
		String data_str = new String(data_bytes);
		String[] data_str_records = data_str.split("Record_");
		data_str_records = Arrays.copyOfRange(data_str_records, 1,
				data_str_records.length);
		for (String str_record : data_str_records) {
			byte[] temp_data_bytes = str_record.getBytes();
			byte[] number = Arrays.copyOfRange(temp_data_bytes, 1, 5);
			byte[] samples = Arrays.copyOfRange(temp_data_bytes, 5,
					temp_data_bytes.length - 1);
			DataContainer dc = new DataContainer(number, samples);
			listModel.addElement(dc.getName());
			record_list.add(dc);
		}
		list.removeAll();
		list.setModel(listModel);
	}

	private class DataWaiter extends Thread {

		int tempDataLength;
		JChartPanel chartPanel;

		public DataWaiter(JChartPanel chart) {
			chartPanel = chart;
		}

		public void run() {
			try {
				while (true) {
					Thread.sleep(500);
					if (tempDataLength == connector.peekLength()) {
						dataReady();
						return;
					} else {
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
