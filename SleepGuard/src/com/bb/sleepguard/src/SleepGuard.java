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
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTree;


public class SleepGuard extends JFrame {

	private JPanel contentPane;
	private SimpleConnector connector;
	private static String[] portNames= SimpleConnector.listAvailablePorts();

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
		
		JChartPanel chartPanel = new JChartPanel();
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
			}
		});
		panel_1.add(btnNewButton, "cell 0 0");
		
		JButton btnNewButton_1 = new JButton("Erase memory");
		panel_1.add(btnNewButton_1, "cell 0 1");
		
		JButton btnNewButton_2 = new JButton("Start measurement");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel_1.add(btnNewButton_2, "cell 0 2");
		
		JButton btnNewButton_3 = new JButton("Stop measurement");
		panel_1.add(btnNewButton_3, "cell 0 3");
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"test", "test2", "test3", "test4", "test5"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(list);
		tabbedPane_1.addTab("Records", null, scrollPane, null);
		
		
		
	}
}
