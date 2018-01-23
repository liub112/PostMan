package com.lfxfs;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.awt.GridBagLayout;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.PopupMenu;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import com.al.angel.invoke.http.HttpInvoke;
import com.al.angel.invoke.http.HttpResponse;
import com.lfxfs.compents.HistoryDialog;
import com.lfxfs.compents.HttpKeepDialog;
import com.lfxfs.dao.httpDao;
import com.lfxfs.model.HttpKeep;
import com.lfxfs.model.HttpRequest;

import java.awt.Color;
import javax.swing.JTree;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;

public class AppView extends JFrame {
	private static final String ENCODING = "UTF-8";
	private Boolean setTimeOut = true;
	private JPanel contentPane;
	public JTextField textField_url;
	private final AppView frame;
	private final httpDao httpDao;
	public final  JTextArea textArea_reqText ; 
	public final  JTextArea textArea_rspText ;
	private final DefaultTreeModel dt;
	private DefaultMutableTreeNode top;
	public  final JTree tree;
	private final JPopupMenu popMenu; //菜单

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/**
					 * http://www.52im.net/thread-26-1-1.html
					 * 皮肤使用介绍网址
					 */
					BeautyEyeLNFHelper.frameBorderStyle =BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
					org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
					UIManager.put("RootPane.setupButtonVisible", false);
					AppView frame = new AppView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/** 
	* 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体 
	*/  
	private static void InitGlobalFont(Font font) {  
		FontUIResource fontRes = new FontUIResource(font);  
		for (Enumeration<Object> keys = UIManager.getDefaults().keys();keys.hasMoreElements(); ) {  
			Object key = keys.nextElement();  
			Object value = UIManager.get(key);  
			if (value instanceof FontUIResource) {  
				UIManager.put(key, fontRes);  
			}  
		} 
	}

	/**
	 * Create the frame.
	 */
	public AppView() {
		frame = this;
		httpDao = new httpDao();
		setTitle("POST-工具v2.0 --by lfxfs");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1087, 708);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setLocationRelativeTo(null);
//		InitGlobalFont(new Font("微软雅黑", Font.PLAIN, 12));
		JLabel lblNewLabel_1 = new JLabel("请求地址");
		lblNewLabel_1.setBounds(251, 26, 57, 23);
		contentPane.add(lblNewLabel_1);
		
		textField_url = new JTextField();
		textField_url.setBounds(374, 26, 537, 23);
		contentPane.add(textField_url);
		textField_url.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("请求");
		
		
		JLabel lblNewLabel_3 = new JLabel("请求内容");
		lblNewLabel_3.setBounds(251, 63, 72, 15);
		contentPane.add(lblNewLabel_3);
		JScrollPane scrollPane_req = new JScrollPane();
		scrollPane_req.setBounds(374, 59, 537, 144);
		contentPane.add(scrollPane_req);
		
		textArea_reqText = new JTextArea();
		scrollPane_req.setViewportView(textArea_reqText);
		textArea_reqText.setBorder(new LineBorder(Color.LIGHT_GRAY));
		textArea_reqText.setWrapStyleWord(true);
		textArea_reqText.setLineWrap(true);
		
		JLabel lblNewLabel_4 = new JLabel("返回内容");
		lblNewLabel_4.setBounds(251, 235, 72, 15);
		contentPane.add(lblNewLabel_4);
		
		textArea_rspText = new JTextArea();
		textArea_rspText.setBounds(305, 218, 537, 345);
		textArea_rspText.setLineWrap(true);
		textArea_rspText.setBorder(new LineBorder(Color.LIGHT_GRAY));
		JScrollPane scrollPane_rsp = new JScrollPane();
		scrollPane_rsp.setBounds(374, 231, 537, 345);
		scrollPane_rsp.setViewportView(textArea_rspText);
		contentPane.add(scrollPane_rsp);
		
		btnNewButton_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent a) {
				try {
					textArea_reqText.setLineWrap(true);
					textArea_rspText.setLineWrap(true);
					HttpRequest req = new HttpRequest();
					String url = textField_url.getText().trim();
					String reqText = textArea_reqText.getText().trim();
					Date start = new Date();
					int timeOut = setTimeOut == true?3600:Integer.MAX_VALUE;					
					HttpResponse resp = HttpInvoke.sendRequest(url, null, reqText,
							timeOut, ENCODING);
					Long cost = new Date().getTime()-start.getTime();
					String respText = resp.getContent().trim();
					frame.textArea_rspText.setText(respText);
					req.setUrl(url);
					req.setReqText(reqText);
					req.setRspText(respText);
					req.setCost(cost);
					httpDao.saveReqInfo(req);

					frame.updateTree(tree);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}

			
			}
		});
		btnNewButton_1.setBounds(988, 26, 47, 23);
		contentPane.add(btnNewButton_1);
		JButton button = new JButton("收藏");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String httpUrl = frame.textField_url.getText();
//				String reqText = frame.textArea_reqText.getText();
//				HttpKeepAdd httpKeepAdd =new HttpKeepAdd(httpUrl,reqText,frame);
//				httpKeepAdd.setVisible(true);
//				frame.contentPane.add(httpKeepAdd);	
				HttpKeepDialog httpKeepDialog = new HttpKeepDialog(frame,true);
				httpKeepDialog.setVisible(true);
				getContentPane().add(httpKeepDialog);
			}
		});
		button.setBounds(562, 586, 93, 23);
//		button.setBounds(984, 26, 47, 23);

		contentPane.add(button);
		
		tree = getTree();
		tree.setBorder(new LineBorder(Color.LIGHT_GRAY));
		tree.setBounds(31, 32, 155, 577);
		dt =(DefaultTreeModel) tree.getModel();
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addMouseListener(new MouseListener() {			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				TreePath path = tree.getPathForLocation(e.getX(), e.getY()); // 关键是这个方法的使用
			 	if (path == null) {  //JTree上没有任何项被选中
			 		return;
			 	}
			 	tree.setSelectionPath(path);
			 	if (e.getButton() == 3) {
			 		popMenu.show(tree, e.getX(), e.getY());
			 	}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//添加菜单项以及为菜单项添加事件

	 	popMenu = new JPopupMenu();
//	 	JMenuItem addItem;   //各个菜单项
	 	JMenuItem delItem;
	 	JMenuItem historyItem;

//	 	addItem = new JMenuItem("添加");
//	 	addItem.addActionListener(new  ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//	 		
//	 	});

	 	delItem = new JMenuItem("删除");
	 	delItem.addActionListener(new  ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();  //获得右键选中的节点
				if (node.isRoot()) {
			  		return;
			  	}
			  	((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
			  	Object o = node.getUserObject();
			  	if(o instanceof HttpKeep){			  		
			  		httpDao.delReqKeepInfo((HttpKeep)o);
			  	}else{
			  		httpDao.delReqInfo((HttpRequest)o);
			  	}
			  	node.getUserObject();
			}	 		
	 	});

	 	historyItem = new JMenuItem("History");
	 	historyItem.addActionListener(new  ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();
				System.out.println(node.toString());
//				HttpHistory history = new HttpHistory(frame);
//            	history.setVisible(true);
//            	frame.contentPane.add(history);		
				HistoryDialog history = new HistoryDialog(frame,true);
            	history.setVisible(true);
            	contentPane.add(history);	
			}
	 		
	 	});

	 

//	 	popMenu.add(addItem);
	 	popMenu.add(historyItem);
	 	popMenu.add(delItem);
		tree.setCellEditor(new DefaultTreeCellEditor(tree,new DefaultTreeCellRenderer()));
		DefaultTreeCellRenderer render=(DefaultTreeCellRenderer)(tree.getCellRenderer());
		URL base = this.getClass().getResource("");
		String path="";
		try {
			path = new File(base.getFile(), "../../").getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(this.getClass().getResource("img/url.png"));
		//叶节点的图标，也就是下面没有子结点的节点图标
		Icon leafIcon=new ImageIcon(this.getClass().getResource("img/url.png"));
		//非叶节点关闭时的图标，也就是下面有子结点的节点图标
		Icon closedIcon=new ImageIcon(this.getClass().getResource("img/文件夹-收起.png"));
		//非叶节点打开时的图标
		Icon openedIcon=new ImageIcon(this.getClass().getResource("img/文件夹-展开.png"));
		render.setLeafIcon(leafIcon);
		render.setClosedIcon(closedIcon);
		render.setOpenIcon(openedIcon);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 32, 185, 577);
		scrollPane.setViewportView(tree);
		contentPane.add(scrollPane);
		

		
		JLabel label = new JLabel("超时设置");
		label.setBounds(31, 7, 54, 15);
		contentPane.add(label);
		ButtonGroup bottonGroup = new ButtonGroup();
		JRadioButton radioButton_1 = new JRadioButton("启用");
		radioButton_1.setBounds(90, 3, 72, 23);
		radioButton_1.setSelected(true);
		radioButton_1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				setTimeOut = true;
				
			}
		});
		bottonGroup.add(radioButton_1);
		contentPane.add(radioButton_1);
		
		JRadioButton radioButton = new JRadioButton("关闭");
		radioButton.setBounds(157, 3, 72, 23);
		radioButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				setTimeOut = false;
				
			}
		});
		bottonGroup.add(radioButton);
		contentPane.add(radioButton);
		// 添加选择事件
		tree.addTreeSelectionListener(new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent e) {
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
		                .getLastSelectedPathComponent();
	 
		        if (node == null)
		            return;
	 
		        Object object = node.getUserObject();
		        if (node.isLeaf()) {
		        	frame.textArea_reqText.setLineWrap(true);
		        	frame.textArea_rspText.setLineWrap(true);
		        	if(object instanceof HttpRequest){
	                	HttpRequest req = (HttpRequest) object;
	                	frame.textField_url.setText(req.getUrl());
	                	frame.textArea_reqText.setText(req.getReqText());
	                	frame.textArea_rspText.setText(req.getRspText());
	                    System.out.println("你选择了：" + req.toString());
		        	}else if(object instanceof HttpKeep){
		        		HttpKeep req = (HttpKeep) object;
		                frame.textField_url.setText(req.getHttpUrl());
		                frame.textArea_reqText.setText(req.getReqText());
		                System.out.println("你选择了：" + req.toString());
		        	}	                    
		        }
	 
		    }
		});
	}
	
	public void  fullJreee(){
		JTree tree = getTree();
		tree.setBorder(new LineBorder(Color.LIGHT_GRAY));
		tree.setBounds(10, 26, 153, 583);
		contentPane.add(tree);
	}
	
	  private  JTree getTree(){
	        // 创建没有父节点和子节点、但允许有子节点的树节点，并使用指定的用户对象对它进行初始化。
	        // public DefaultMutableTreeNode(Object userObject)
	        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("收藏夹");
	        List<HttpKeep> httpKeeps = httpDao.queryReqKeepInfo();
	        if(httpKeeps!=null&&httpKeeps.size()>0){
		        for (HttpKeep httpKeep : httpKeeps) {
		        	node1.add(new DefaultMutableTreeNode(httpKeep));
				}
	        }

	        DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("调用历史");
	        List<HttpRequest> httpRequests = httpDao.queryReqInfo();
	        if(httpRequests!=null&&httpRequests.size()>0){
		        for (HttpRequest httpRequest : httpRequests) {
		        	node2.add(new DefaultMutableTreeNode(httpRequest));
				}
	        }
	 
	        top = new DefaultMutableTreeNode("POST-工具");	 
	        top.add(node1);
	        top.add(node2);
	        final JTree tree = new JTree(top);		 
	        // 添加选择事件
	        tree.addTreeSelectionListener(new TreeSelectionListener() {	 

				@Override
				public void valueChanged(TreeSelectionEvent e) {
	                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
	                        .getLastSelectedPathComponent();
	 
	                if (node == null)
	                    return;
	 
	                Object object = node.getUserObject();
	                if (node.isLeaf()) {
	                	frame.textArea_reqText.setLineWrap(true);
	                	frame.textArea_rspText.setLineWrap(true);
	                	if(object instanceof HttpRequest){
		                	HttpRequest req = (HttpRequest) object;
		                	frame.textField_url.setText(req.getUrl());
		                	frame.textArea_reqText.setText(req.getReqText());
		                	frame.textArea_rspText.setText(req.getRspText());
		                    System.out.println("你选择了：" + req.toString());
	                	}else if(object instanceof HttpKeep){
	                		HttpKeep req = (HttpKeep) object;
		                	frame.textField_url.setText(req.getHttpUrl());
		                	frame.textArea_reqText.setText(req.getReqText());
		                	frame.textArea_rspText.setText("");
		                    System.out.println("你选择了：" + req.toString());
	                	}	                    
	                }	 
	            }
	        });
	        return tree;
	    }
	  
	  public void treeReload(){
		  DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("收藏夹");
	        List<HttpKeep> httpKeeps = httpDao.queryReqKeepInfo();
	        if(httpKeeps!=null&&httpKeeps.size()>0){
		        for (HttpKeep httpKeep : httpKeeps) {
		        	node1.add(new DefaultMutableTreeNode(httpKeep));
				}
	        }

	        DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("调用历史");
	        List<HttpRequest> httpRequests = httpDao.queryReqInfo();
	        if(httpRequests!=null&&httpRequests.size()>0){
		        for (HttpRequest httpRequest : httpRequests) {
		        	node2.add(new DefaultMutableTreeNode(httpRequest));
				}
	        }	
	        top.add(node1);
	        top.add(node2);
	  }
	  
	  
	  /***
	   * 刷新树，不更改树原来的展开状态
	   */
	public void updateTree(JTree tree){
	   Vector<TreePath> v=new Vector<TreePath>();
	   getExpandNode(tree,top, v);
	   top.removeAllChildren();
	   treeReload(); 
	   dt.reload();
	   
	   int n=v.size();
	   for(int i=0;i<n;i++){
	    Object[] objArr=v.get(i).getPath();
	    Vector<Object> vec=new Vector<Object>();
	    int len=objArr.length;
	    for(int j=0;j<len;j++){
	     vec.add(objArr[j]);
	    }
	    expandNode(tree,top,vec);
	   }
	 }
	 
	 public Vector<TreePath> getExpandNode(JTree tree,TreeNode node,Vector<TreePath> v){
		  if (node.getChildCount() > 0) {
		      TreePath treePath=new TreePath(dt.getPathToRoot(node));
		      if(tree.isExpanded(treePath)) v.add(treePath);
		            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
		                TreeNode n = (TreeNode)e.nextElement();
		                getExpandNode(tree,n,v);
		            }
		        }
		  return v;
		}
	 
	 /**
	  * 
	  * @param myTree  树
	  * @param currNode  展开节点的父节点
	  * @param vNode 展开节点，路径字符串|路径Node组成的Vector，按从根节点开始，依次添加到Vector
	  */
	  void expandNode(JTree myTree,DefaultMutableTreeNode currNode, Vector<Object> vNode){
	   if(currNode.getParent()==null){
	    vNode.removeElementAt(0);
	   }
	   if(vNode.size()<=0) return;
	   
	      int childCount = currNode.getChildCount();
	      String strNode = vNode.elementAt(0).toString();
	      DefaultMutableTreeNode child = null;
	      boolean flag=false;
	      for(int i=0; i<childCount; i++){
	        child = (DefaultMutableTreeNode)currNode.getChildAt(i);
	        if(strNode.equals(child.toString())){
	         flag=true;
	         break;
	        }
	      }
	      if(child != null&&flag){
	        vNode.removeElementAt(0);
	         if(vNode.size()>0){
	          expandNode(myTree,child, vNode);
	         }else{
	          myTree.expandPath(new TreePath(child.getPath()));
	         }
	      }
	    } 
	  
	  public void addNode(DefaultMutableTreeNode node,int n){
		  for(int i=1;i<=n;i++){
		    DefaultMutableTreeNode newChild=new DefaultMutableTreeNode(i);
		     for(int m=1;m<=n;m++){
		      DefaultMutableTreeNode newChild2=new DefaultMutableTreeNode(m);
		      for(int j=1;j<=n;j++){
		DefaultMutableTreeNode newChild3=new DefaultMutableTreeNode(m);
		       newChild2.add(newChild3);
		      }
		      newChild.add(newChild2);
		     }
		    node.add(newChild);
		  }
		}
}
