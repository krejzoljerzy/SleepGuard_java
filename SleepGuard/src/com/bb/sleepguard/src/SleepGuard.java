package com.bb.sleepguard.src;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;

import java.awt.Window.Type;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ImageIcon;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;

public class SleepGuard extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static SimpleConnector connector;
	private static String[] portNames = SimpleConnector.listAvailablePorts();
//	private static JChartPanel chartPanel;
	private static JChartPanel chartPanel;
	private static JComboBox<String> comboBox;
	private static ArrayList<DataContainer> record_list = new ArrayList<DataContainer>();
	private static JList<String> list;
	private static JFileChooser chooser;
	private static String choosertitle;
	private SleepGuard handle;
	private static JCheckBox chckbxRemoveMeanFrom;
	private static JCheckBox chckbxNewCheckBox_1;
	private static JFormattedTextField formattedTextField_1;
	private static JFormattedTextField formattedTextField;
	private static File file = null;
	private static boolean firstSelection = true;
	private static int last_selected_index = -1;
	private static int last_partial_selected_index = -1;
	private static JTabbedPane tabbedPane_1;
	private static JButton btnOpen;
	private static JButton btnClose;
	private static Properties props = new Properties();
	private static String propfile_name = "settings";
	private static ArrayList<DataContainer> cropped_data;
	private static JList<String> list_1;

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
					frame.setResizable(false);

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

		getPath();

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(13, 12, 695, 537);
		contentPane.add(tabbedPane);

		chartPanel = new JChartPanel();
		//chartPanel = new JChartPanel();
		tabbedPane.addTab("Chart", null, chartPanel, null);
		FlowLayout flowLayout = (FlowLayout) chartPanel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);

		tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(720, 12, 263, 537);
		contentPane.add(tabbedPane_1);

		JPanel panel = new JPanel();
		tabbedPane_1.addTab("Connection", null, panel, null);

		comboBox = new JComboBox(portNames);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
			}
		});
		panel.setLayout(new MigLayout("", "[188.00px,grow][35]",
				"[35px][35px][35]"));
		panel.add(comboBox, "cell 0 0,grow");

		btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connector = new SimpleConnector(portNames[comboBox
						.getSelectedIndex()]);
				if (connector.isOpened()) {

					tabbedPane_1.setEnabledAt(2, connector.isOpened());
					btnClose.setEnabled(connector.isOpened());
					btnOpen.setEnabled(!connector.isOpened());

				}
			}
		});

		JButton btnNewButton_6 = new JButton("");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				portNames = SimpleConnector.listAvailablePorts();
				comboBox.removeAllItems();
				for (String s : portNames) {
					comboBox.addItem(s);
				}
				handle.validate();

			}
		});
		btnNewButton_6.setIcon(new ImageIcon(SleepGuard.class
				.getResource("/com/bb/sleepguard/images/refresh_1.png")));
		panel.add(btnNewButton_6, "cell 1 0");
		panel.add(btnOpen, "cell 0 1 2 1,grow");

		btnClose = new JButton("Close");
		btnClose.setEnabled(false);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (connector.isOpened()) {
					connector.close();
					if (!connector.isOpened()) {
						btnClose.setEnabled(false);
						btnOpen.setEnabled(true);
					}
				}
			}
		});
		panel.add(btnClose, "cell 0 2 2 1,grow");

		JPanel panel_3 = new JPanel();
		tabbedPane_1.addTab("Records", null, panel_3, null);
		list = new JList(new String[] { "No records - download from device" });
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				System.out.println(arg0.getValueIsAdjusting());
				if (!arg0.getValueIsAdjusting()) {
					int selected_index = list.getSelectedIndex();
					if (last_selected_index != selected_index
							&& selected_index > -1) {
						last_selected_index = selected_index;
						if (record_list.size() > 0) {
							chartPanel.add(record_list.get(selected_index)
									.getData());
							tabbedPane_1.setEnabledAt(3, true);

						}
					}
				}

			}
		});
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// chartPanel.add(record_list.get(list.getSelectedIndex())
				// .getData());

			}
		});
		panel_3.setLayout(new MigLayout("", "[120px,grow]",
				"[500px][100px,grow]"));

		JScrollPane scrollPane = new JScrollPane(list);
		panel_3.add(scrollPane, "cell 0 0,grow");

		JPanel panel_2 = new JPanel();
		panel_3.add(panel_2, "cell 0 1,grow");
		panel_2.setLayout(new MigLayout("", "[150px][150px]", "[32px]"));

		JButton btnNewButton_4 = new JButton("Save records");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				chooser = new JFileChooser();
				if (file != null) {
					chooser.setCurrentDirectory(file);
				} else {
					chooser.setCurrentDirectory(new java.io.File("."));
				}

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
					String folder = String.format("%02d",
							(calendar.get(Calendar.HOUR_OF_DAY)))
							+ ".";
					folder += String.format("%02d",
							(calendar.get(Calendar.MINUTE)))
							+ "-";
					folder += String.format("%02d",
							(calendar.get(Calendar.DAY_OF_MONTH)))
							+ "-";
					folder += String.format("%02d",
							(calendar.get(Calendar.MONTH)))
							+ "-";
					folder += Integer.toString(calendar.get(Calendar.YEAR));
					file = new File(chooser.getSelectedFile().getAbsolutePath()
							+ "-" + folder);
					String path = file.getAbsolutePath();
					savePath(path);
					if (file.exists()) {
						return;
					}
					file.mkdirs();
					if (record_list.size() > 0) {
						for (DataContainer d : record_list) {
							file = new File(path + "/" + d.getName() + ".slg");
							try {
								FileOutputStream fos = new FileOutputStream(
										file);
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
				last_selected_index=-1;
				chooser = new JFileChooser();
				if (file != null) {
					chooser.setCurrentDirectory(file);
				} else {
					chooser.setCurrentDirectory(new File("."));
				}
				chooser.setDialogTitle(choosertitle);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileFilter filter = new FileNameExtensionFilter(
						"SleepGuard files", "slg");
				chooser.addChoosableFileFilter(filter);
				chooser.setMultiSelectionEnabled(true);
				//
				// disable the "All files" option.
				//

				//
				if (chooser.showOpenDialog(handle) == JFileChooser.APPROVE_OPTION) {

					File[] files = chooser.getSelectedFiles();
					file = files[0].getParentFile();
					savePath(file.getAbsoluteFile().toString());
					if (files != null) {
						if (files.length > 0) {
							record_list.clear();
							FileInputStream fis;
							for (File f : files) {
								try {
									fis = new FileInputStream(f);
									byte[] data = new byte[(int) f.length()];
									fis.read(data);
									record_list.add(new DataContainer(f
											.getName().substring(0,
													f.getName().length() - 4),
											data));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							DefaultListModel<String> listModel = new DefaultListModel<String>();
							for (DataContainer d : record_list) {
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

		JPanel panel_1 = new JPanel();
		tabbedPane_1.addTab("Commands", null, panel_1, null);
		tabbedPane_1.setEnabledAt(2, false);
		panel_1.setLayout(new MigLayout("", "[280px,center]",
				"[30px:35px:40px][30px:35px:40px][30px:35px:40px][30px:35px:40px]"));

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

		JPanel panel_4 = new JPanel();
		tabbedPane_1.addTab("Analize", null, panel_4, null);
		tabbedPane_1.setEnabledAt(3, false);
		panel_4.setLayout(null);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(new EtchedBorder(
				EtchedBorder.LOWERED, null, null), "Analize toolbox",
				TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_6.setBounds(12, 12, 233, 273);
		panel_4.add(panel_6);
		panel_6.setLayout(new MigLayout("", "[169.00,grow]", "[20][30][][30][30][30][25][25]"));

		JLabel lblPressureTreshhold = new JLabel("Pressure treshhold [mBar]");
		panel_6.add(lblPressureTreshhold,
				"cell 0 0,alignx center,aligny center");

		DecimalFormat decimalFormat = new DecimalFormat("0.##");
		decimalFormat.setGroupingUsed(false);
		formattedTextField = new JFormattedTextField(decimalFormat);
		formattedTextField.setText("0,5");
		panel_6.add(formattedTextField, "cell 0 1,grow");

		JLabel lblTimeTreshold = new JLabel("Time treshold [s]");
		panel_6.add(lblTimeTreshold, "cell 0 2,alignx center,aligny center");

		formattedTextField_1 = new JFormattedTextField(decimalFormat);
		formattedTextField_1.setText("10");
		panel_6.add(formattedTextField_1, "cell 0 3,grow");

		JButton btnNewButton_5 = new JButton("Look for apnea symptoms");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				last_partial_selected_index=-1;
				Analizer anal /* lol */= new Analizer();
				double press_treshold = Double.parseDouble(formattedTextField
						.getText().replace(',', '.'));
				double time_treshold = Double.parseDouble(formattedTextField_1
						.getText().replace(',', '.'));
				cropped_data = anal.startAnalizing(
						record_list.get(list.getSelectedIndex()),
						press_treshold, time_treshold);
				int breakpoint_test = 0;
				DefaultListModel<String> listModel_1 = new DefaultListModel<String>();
				for (DataContainer d : cropped_data) {
					listModel_1.addElement(d.getName());
				}
				list_1.removeAll();
				list_1.setModel(listModel_1);

			}
		});
		panel_6.add(btnNewButton_5, "cell 0 4,grow");

		JButton btnResetDataSet = new JButton("Reset data set");
		btnResetDataSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/* Get selected record */
				int index = list.getSelectedIndex();
				DataContainer temp = record_list.get(index);

				if (!chckbxRemoveMeanFrom.isSelected()) {
					chartPanel.add(temp
							.getData(DataContainer.RAW));
				} else {
					chartPanel.add(temp.getData(DataContainer.NO_MEAN));
				}
			}
			
		});
		panel_6.add(btnResetDataSet, "cell 0 5,grow");

		chckbxNewCheckBox_1 = new JCheckBox("Integrate pressure signal");
		panel_6.add(chckbxNewCheckBox_1, "cell 0 6");

		chckbxRemoveMeanFrom = new JCheckBox("Remove mean from signal");
		panel_6.add(chckbxRemoveMeanFrom, "cell 0 7");
		chckbxRemoveMeanFrom.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {

				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					/* Get selected record */
					int index = list.getSelectedIndex();
					DataContainer temp = record_list.get(index).clone();

					if (chckbxNewCheckBox_1.isSelected()) {
						chartPanel.add(temp
								.getData(DataContainer.INTEGRATED_AND_NO_MEAN));
					} else {
						chartPanel.add(temp.getData(DataContainer.NO_MEAN));
					}
				} else if (arg0.getStateChange() == ItemEvent.DESELECTED) {

					/* Get selected record */
					int index = list.getSelectedIndex();
					DataContainer temp = record_list.get(index).clone();

					if (chckbxNewCheckBox_1.isSelected()) {
						chartPanel.add(temp.getData(DataContainer.INTEGRATED));
					} else {
						chartPanel.add(temp.getData(DataContainer.RAW));
					}

				}

			}
		});
		chckbxNewCheckBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					/* Get selected record */
					int index = list.getSelectedIndex();
					DataContainer temp = record_list.get(index).clone();

					if (chckbxRemoveMeanFrom.isSelected()) {
						chartPanel.add(temp
								.getData(DataContainer.INTEGRATED_AND_NO_MEAN));
					} else {
						chartPanel.add(temp.getData(DataContainer.INTEGRATED));
					}

				} else if (arg0.getStateChange() == ItemEvent.DESELECTED) {

					/* Get selected record */
					int index = list.getSelectedIndex();
					DataContainer temp = record_list.get(index).clone();

					if (chckbxRemoveMeanFrom.isSelected()) {
						chartPanel.add(temp.getData(DataContainer.NO_MEAN));
					} else {
						chartPanel.add(temp.getData(DataContainer.RAW));
					}

				}
			}
		});

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(new TitledBorder(new EtchedBorder(
				EtchedBorder.LOWERED, null, null), "Cropped data list",
				TitledBorder.CENTER, TitledBorder.TOP, null, null), "",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_7.setBounds(12, 287, 233, 211);
		panel_4.add(panel_7);
		panel_7.setLayout(new MigLayout("", "[220px]", "[287px]"));


		
		list_1 = new JList<String>();
		list_1.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				System.out.println(arg0.getValueIsAdjusting());
				if (!arg0.getValueIsAdjusting()) {
					int selected_index = list_1.getSelectedIndex();
					if (last_partial_selected_index != selected_index
							&& selected_index > -1) {
						last_partial_selected_index = selected_index;
						if (record_list.size() > 0) {
							chartPanel.add(cropped_data.get(selected_index)
									.getData(),cropped_data.get(selected_index).getBeginning());
							

						}
					}
				}

			}
		});
		list_1.setBounds(0, 0, 0, 0);
		list_1.setModel(new AbstractListModel() {
			String[] values = new String[] {"No data, please start data analize"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane_1 = new JScrollPane(list_1);
		panel_7.add(scrollPane_1, "cell 0 0,grow");

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

	private static void savePath(String path) {
		OutputStream output = null;
		try {

			output = new FileOutputStream(propfile_name);

			// set the properties value
			props.setProperty("path", path);

			// save properties to project root folder
			props.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private static void getPath() {
		InputStream input = null;

		try {

			input = new FileInputStream(propfile_name);

			// load a properties file
			props.load(input);

			// get the property value and print it out
			System.out.println(props.getProperty("path"));

		} catch (IOException ex) {
			/* Prop file does not exist */
			savePath(new File(".").getAbsolutePath());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		file = new File(props.getProperty("path"));
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
