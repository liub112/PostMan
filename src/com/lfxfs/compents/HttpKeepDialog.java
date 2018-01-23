package com.lfxfs.compents;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.springframework.util.StringUtils;

import com.lfxfs.AppView;
import com.lfxfs.model.HttpKeep;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HttpKeepDialog extends JDialog {
	private  JPanel contentPanel = new JPanel();
	private  JTextField textField_name;
	private  JTextField textField_url;
	private  JTextArea textArea_reqText;
	private com.lfxfs.dao.httpDao httpDao;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			HttpKeepDialog dialog = new HttpKeepDialog(null,false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public HttpKeepDialog(JFrame parent,Boolean mode) {
		super(parent,mode);
		final AppView appView = (AppView)parent;
		httpDao = new com.lfxfs.dao.httpDao();
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setLocationRelativeTo(getOwner()); 
		JLabel lblNewLabel = new JLabel("请求别名");
		
		JLabel lblNewLabel_1 = new JLabel("请求地址");
		
		JLabel lblNewLabel_2 = new JLabel("请求入参");
		
		textField_name = new JTextField();		
		textField_name.setColumns(10);
		
		
		textField_url = new JTextField();
		textField_url.setColumns(10);
		textField_url.setText(appView==null?"":appView.textField_url.getText());
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 26, 79, 19);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(25)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addComponent(lblNewLabel_1)
						.addComponent(lblNewLabel_2))
					.addGap(26)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(scrollPane)
						.addComponent(textField_url)
						.addComponent(textField_name, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
					.addContainerGap(79, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(19)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(textField_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(30)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(textField_url, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_2)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(42, Short.MAX_VALUE))
		);
		
		final JTextArea textArea_reqText = new JTextArea();
		textArea_reqText.setText(appView==null?"":appView.textArea_reqText.getText());
		scrollPane.setViewportView(textArea_reqText);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("收藏");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String httpName =textField_name.getText();
						String httpUrl =textField_url.getText();
						String reqText =textArea_reqText.getText();
						if(!StringUtils.hasLength(httpName)){
							JOptionPane.showMessageDialog(null, "名称不允许为空");
						}
						if(!StringUtils.hasLength(httpUrl)){
							JOptionPane.showMessageDialog(null, "url不允许为空");
						}
						if(!StringUtils.hasLength(reqText)){
							JOptionPane.showMessageDialog(null, "请求内容不允许为空");
						}
						HttpKeep keep = new HttpKeep(httpName, httpUrl, reqText);
						httpDao.saveReqKeepInfo(keep);
						if(appView!=null)
							appView.updateTree(appView.tree);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("取消");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
