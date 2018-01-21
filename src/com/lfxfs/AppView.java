package com.lfxfs;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.PopupMenu;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.springframework.util.StringUtils;

import com.al.angel.invoke.http.HttpInvoke;
import com.al.angel.invoke.http.HttpResponse;
import com.lfxfs.compents.HttpKeepAdd;
import com.lfxfs.dao.httpDao;
import com.lfxfs.model.HttpKeep;
import com.lfxfs.model.HttpRequest;

import java.awt.Color;
import javax.swing.JTree;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;

public class AppView extends JFrame {
	private static final String ENCODING = "UTF-8";

	private JPanel contentPane;
	private JTextField textField_url;
	private final AppView frame;
	private final httpDao httpDao;
	private final  JTextArea textArea_reqText ; 
	private final  JTextArea textArea_rspText ;
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
					AppView frame = new AppView();
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
	public AppView() {
		frame = this;
		httpDao = new httpDao();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 949, 658);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setLocationRelativeTo(null);
		
		JLabel lblNewLabel_1 = new JLabel("地址");
		lblNewLabel_1.setBounds(211, 26, 57, 23);
		contentPane.add(lblNewLabel_1);
		
		textField_url = new JTextField();
		textField_url.setBounds(305, 26, 537, 23);
		contentPane.add(textField_url);
		textField_url.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("请求");
		
		
		JLabel lblNewLabel_3 = new JLabel("请求内容");
		lblNewLabel_3.setBounds(211, 54, 72, 15);
		contentPane.add(lblNewLabel_3);
		
		textArea_reqText = new JTextArea();
		textArea_reqText.setBounds(305, 59, 537, 144);
		textArea_reqText.setBorder(new LineBorder(Color.LIGHT_GRAY));
		textArea_reqText.setWrapStyleWord(true);
		textArea_reqText.setLineWrap(true);
		contentPane.add(textArea_reqText);
		
		JLabel lblNewLabel_4 = new JLabel("返回内容");
		lblNewLabel_4.setBounds(211, 221, 72, 15);
		contentPane.add(lblNewLabel_4);
		
		textArea_rspText = new JTextArea();
		textArea_rspText.setBounds(305, 218, 537, 345);
		textArea_rspText.setLineWrap(true);
		textArea_rspText.setBorder(new LineBorder(Color.LIGHT_GRAY));
		contentPane.add(textArea_rspText);
		
		btnNewButton_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent a) {
				try {
					frame.textArea_reqText.setLineWrap(true);
					frame.textArea_rspText.setLineWrap(true);
					HttpRequest req = new HttpRequest();
					String url = frame.textField_url.getText().trim();
					String reqText = frame.textArea_reqText.getText().trim();
					
					HttpResponse resp = HttpInvoke.sendRequest(url, null, reqText,
							3600, frame.ENCODING);
					String respText = resp.getContent().trim();
					frame.textArea_rspText.setText(respText);
					req.setUrl(url);
					req.setReqText(reqText);
					req.setRspText(respText);
					httpDao.saveReqInfo(req);

					frame.updateTree(tree);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}

			
			}
		});
		btnNewButton_1.setBounds(866, 26, 67, 23);
		contentPane.add(btnNewButton_1);
		JButton button = new JButton("收藏");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String httpUrl = frame.textField_url.getText();
				String reqText = frame.textArea_reqText.getText();
//				if(!StringUtils.hasLength(httpUrl)){
//					JOptionPane.showMessageDialog(null, "请求地址为空！");
//				}
				HttpKeepAdd httpKeepAdd =new HttpKeepAdd(httpUrl,reqText,frame);
				httpKeepAdd.setVisible(true);
				frame.contentPane.add(httpKeepAdd);				
			}
		});
		button.setBounds(562, 586, 93, 23);
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
//	 	JMenuItem editItem;

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

//	 	editItem = new JMenuItem("修改");
//	 	editItem.addActionListener(new  ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//	 		
//	 	});

	 

//	 	popMenu.add(addItem);
	 	popMenu.add(delItem);
//	 	popMenu.add(editItem);
		tree.setCellEditor(new DefaultTreeCellEditor(tree,new DefaultTreeCellRenderer()));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 32, 155, 577);
		scrollPane.setViewportView(tree);
		contentPane.add(scrollPane);
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
		        	}else{
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
	                	}else{
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
