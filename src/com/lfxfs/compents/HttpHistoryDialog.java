package com.lfxfs.compents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.lfxfs.AppView;
import com.lfxfs.dao.httpDao;
import com.lfxfs.model.HttpRequest;

public class HttpHistoryDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table_1;
	private httpDao httpDao = new com.lfxfs.dao.httpDao();
	private final AppView app;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			HttpHistoryDialog dialog = new HttpHistoryDialog(new AppView(),false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public HttpHistoryDialog(JFrame parent,Boolean mode) {
//		super(parent,mode);
		app=(AppView)parent;
		setBounds(100, 100, 726, 442);
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		setLocationRelativeTo(getOwner()); 
		List<HttpRequest> httpRequests = httpDao.queryReqInfo();
		
		JScrollPane scrollPane = new JScrollPane();		
		table_1 = new JTable();		
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() ==2){
					int column = table_1.getSelectedColumn();
					HttpRequest http = (HttpRequest)table_1.getValueAt(column, 1);
					app.textField_url.setText(http.getUrl());
					app.textArea_reqText.setText(http.getReqText());
					app.textArea_rspText.setText(http.getRspText());
					dispose();
				}
			}
		});
		table_1.setSurrendersFocusOnKeystroke(true);
		table_1.setColumnSelectionAllowed(true);
		table_1.setCellSelectionEnabled(true);
		table_1.setBorder(new LineBorder(Color.LIGHT_GRAY));
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
				{null, null, null},
				{null, null, null},
			},
			new String[] {
				"请求 方式", "请求地址", "请求时间"
			}
		){
			boolean[] canEdit = new boolean[] { false, false, false, false};

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		TableColumn firsetColumn = table_1.getColumnModel().getColumn(0);
		firsetColumn.setPreferredWidth(50);
		firsetColumn.setMaxWidth(50);
		firsetColumn.setMinWidth(50);
		TableColumn twoColumn = table_1.getColumnModel().getColumn(1);
		twoColumn.setPreferredWidth(50);
		twoColumn.setMaxWidth(500);
		twoColumn.setMinWidth(50);
		TableColumn threeColumn = table_1.getColumnModel().getColumn(2);
		threeColumn.setPreferredWidth(50);
		threeColumn.setMaxWidth(137);
		threeColumn.setMinWidth(50);
//		table_1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		fullData(httpRequests);
		scrollPane.setViewportView(table_1);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, 0, GroupLayout.PREFERRED_SIZE)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(31)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 637, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, 0, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE))
		);
		getContentPane().setLayout(groupLayout);
		GroupLayout gl_contentPane = new GroupLayout(getContentPane());
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(31)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 637, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(32, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(29, Short.MAX_VALUE))
		);
		getContentPane().setLayout(gl_contentPane);
		

	}
	
	private void fullData(List<HttpRequest> lists) {
		DefaultTableModel dtm = (DefaultTableModel) table_1.getModel();
		dtm.getDataVector().removeAllElements(); 
		for (int i = 1; i < lists.size(); i++) {
			HttpRequest row = lists.get(i);
			Vector v = new Vector();
			v.add("["+row.getCost()+"]");
			v.add(row);
//			v.add(row.getReqText());
			v.add(row.getReqDate());
			dtm.addRow(v);
		}
	}
}
