package com.lfxfs.compents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.springframework.util.StringUtils;

import com.lfxfs.AppView;
import com.lfxfs.dao.httpDao;
import com.lfxfs.model.HttpKeep;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFrame;

public class HttpKeepAdd extends JFrame {
	private final HttpKeepAdd frame;
	private final AppView fatherframe;
	private JTextField textField_name;
	private JTextField textField_url;
	private JTextArea textArea_reqText;
	private final httpDao httpDao;

	/**
	 * Create the frame.
	 */
//	public HttpKeepAdd() {
//		super();
//		frame = this;
//		httpDao = new httpDao();
//		fatherframe = (AppView) jFrame;
//	}
	
	public HttpKeepAdd(String url,String reqText,JFrame jFrame){
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		setLocationRelativeTo(getOwner()); 

		frame = this;
		fatherframe = (AppView) jFrame;
		httpDao = new httpDao();
		
		JLabel label = new JLabel("请求别名");
		label.setBounds(47, 27, 54, 15);
		getContentPane().add(label);
		JLabel label_1 = new JLabel("请求地址");
		label_1.setBounds(47, 73, 54, 15);
		getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("请求内容");
		label_2.setBounds(47, 122, 54, 15);
		getContentPane().add(label_2);
		
		textField_name = new JTextField();
		textField_name.setBounds(111, 24, 220, 21);
		getContentPane().add(textField_name);
		textField_name.setColumns(10);
		
		textField_url = new JTextField();
		textField_url.setBounds(111, 70, 220, 21);
		textField_url.setText(url==null?"http://":url);
		getContentPane().add(textField_url);
		textField_url.setColumns(10);
		
		textArea_reqText = new JTextArea();
		textArea_reqText.setBounds(111, 118, 220, 83);
		textArea_reqText.setWrapStyleWord(true);
		textArea_reqText.setText(reqText==null?"{}":reqText);
		textArea_reqText.setBorder(new LineBorder(Color.LIGHT_GRAY));
		getContentPane().add(textArea_reqText);
		
		JButton btn_submit = new JButton("提交");
		btn_submit.setBounds(105, 237, 93, 23);
		btn_submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String httpName = frame.textField_name.getText();
				String httpUrl = frame.textField_url.getText();
				String reqText = frame.textArea_reqText.getText();
				if(!StringUtils.hasLength(httpName)){
					JOptionPane.showMessageDialog(frame, "名称不允许为空");
				}
				if(!StringUtils.hasLength(httpUrl)){
					JOptionPane.showMessageDialog(frame, "url不允许为空");
				}
				if(!StringUtils.hasLength(reqText)){
					JOptionPane.showMessageDialog(frame, "请求内容不允许为空");
				}
				HttpKeep keep = new HttpKeep(null,httpName, httpUrl, reqText);
				httpDao.saveReqKeepInfo(keep);
				fatherframe.updateTree(fatherframe.tree);
				frame.dispose();
			}
		});
		getContentPane().add(btn_submit);
		
		JButton btn_reset = new JButton("取消");
		btn_reset.setBounds(208, 237, 93, 23);
		btn_reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();				
			}
			
		});
		getContentPane().add(btn_reset);

	} 
}
