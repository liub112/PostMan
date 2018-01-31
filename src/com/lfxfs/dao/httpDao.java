package com.lfxfs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.lfxfs.model.DBInfoDto;
import com.lfxfs.model.HttpKeep;
import com.lfxfs.model.HttpRequest;
import com.lfxfs.util.DbUtil;

public class httpDao {
	private Boolean exists = false;
	private String htttpHeader;
	
	/**
	 * 保存post历史
	 * 
	 */
	public void saveReqInfo(HttpRequest req) {
		Connection conn =null;
		try {			
			conn = DbUtil.getConnection();
			String sql ="insert into http_request values(null,?,?,?,?,?,?)";
			createTableIfNotExist(conn);
			PreparedStatement prtm = conn.prepareStatement(sql);
			prtm.setString(1, req.getUrl());
			prtm.setString(2,req.getReqText());
			prtm.setString(3,req.getRspText());
			prtm.setString(4,req.getHttpHeader());
			prtm.setTimestamp(5, new Timestamp(new Date().getTime()));
			prtm.setLong(6, req.getCost());
			prtm.executeUpdate();
			prtm.close();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
		}finally{
			DbUtil.closeConnection(conn);
		}
		
	}
	
	/**
	 * 保存收藏信息
	 * @param req
	 */
	public void saveReqKeepInfo(HttpKeep req){
		Connection conn =null;
		try {			
			conn = DbUtil.getConnection();
			String sql ="insert into http_keep values(?,?,?,?)";
			createTableIfNotExist(conn);
			PreparedStatement prtm = conn.prepareStatement(sql);
			prtm.setString(1, req.getHttpName());
			prtm.setString(2,req.getHttpUrl());
			prtm.setString(3,req.getReqText());
			prtm.setTimestamp(4,new Timestamp(new Date().getTime()));
			prtm.executeUpdate();
			prtm.close();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "保存收藏异常："+e.getMessage());
		}finally{
			DbUtil.closeConnection(conn);
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	public List<HttpKeep> queryReqKeepInfo() {
		List<HttpKeep> lists = new ArrayList<HttpKeep>();
		Connection conn = null;
		String sql ="select * from http_keep order by create_date desc";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			conn = DbUtil.getConnection();
			createTableIfNotExist(conn);
			PreparedStatement prtm = conn.prepareStatement(sql);
			ResultSet rs = prtm.executeQuery();
			while(rs.next()){
				String httpName=	rs.getString(1);
				String httpUrl = rs.getString(2);
				String rspText = rs.getString(3);
				Timestamp httpDate = rs.getTimestamp(4);	
				HttpKeep dto = new HttpKeep(httpName,httpUrl,rspText);
				lists.add(dto);
			}
			rs.close();
			prtm.close();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "查询收藏异常："+e.getMessage());
		}finally{
			DbUtil.closeConnection(conn);
		}
		return lists;
		
	}

	public List<HttpRequest> queryReqInfo() {
		List<HttpRequest> lists = new ArrayList<HttpRequest>();
		Connection conn = null;
		String  delteSql = "delete from http_request where id not in("
				+ "select id from (select id from http_request order by create_date desc) limit 0,200)";
		String sql ="select * from http_request order by create_date desc";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			conn = DbUtil.getConnection();
			createTableIfNotExist(conn);
			PreparedStatement deletePrtm = conn.prepareStatement(delteSql);
			deletePrtm.executeUpdate();
			PreparedStatement prtm = conn.prepareStatement(sql);
			ResultSet rs = prtm.executeQuery();
			while(rs.next()){
				Long id = rs.getLong(1);
				String url=	rs.getString(2);
				String reqText = rs.getString(3);
				String rspText = rs.getString(4);
				String htttpHeader = rs.getString(5);
				Timestamp httpDate = rs.getTimestamp(6);	
				Long cost = rs.getLong(7);
				HttpRequest dto = new HttpRequest(id,url,htttpHeader,reqText,rspText,sdf.format(new Date(httpDate.getTime())),cost);
				lists.add(dto);
			}
			rs.close();
			prtm.close();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "查询历史异常："+e.getMessage());

		}finally{
			DbUtil.closeConnection(conn);
		}
		return lists;
		
	}
	
	private void createTableIfNotExist(Connection conn) throws Exception{
		if(!exists){
			synchronized(exists){
				String ddl ="CREATE TABLE if not exists http_request ("+
				  "id integer PRIMARY KEY autoincrement, "+
				  "url         VARCHAR (200),"+
				  " req_text    VARCHAR (4000),"+
				  "rsp_text    VARCHAR (4000),"+
				  " http_header VARCHAR (800),"+
				  " create_date DATE    DEFAULT sysdate,"+
				  "cost    DECIMAL(32)"+
				")";
				PreparedStatement prtm = conn.prepareStatement(ddl);
				prtm.executeUpdate();		

				String ddl2 ="CREATE TABLE if not exists http_keep ("+
						"http_name   VARCHAR (200) UNIQUE ON CONFLICT REPLACE PRIMARY KEY,"+
						"http_url         VARCHAR (200),"+
						" req_text    VARCHAR (4000),"+
						" create_date DATE    DEFAULT sysdate"+
						")";
				PreparedStatement prtm2 = conn.prepareStatement(ddl2);
				prtm2.executeUpdate();	
				exists = true;
				prtm.close();
				
			}
		}

	}
	public void delReqKeepInfo(HttpKeep o) {
		Connection conn = null;
		String sql ="delete from http_keep where http_name=?";
		try {
			conn = DbUtil.getConnection();
			createTableIfNotExist(conn);
			PreparedStatement prtm = conn.prepareStatement(sql);
			prtm.setString(1, o.getHttpName());
			prtm.executeUpdate();
			prtm.close();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "删除异常："+e.getMessage());

		}finally{
			DbUtil.closeConnection(conn);
		}
		
	}

	public void delReqInfo(HttpRequest o) {
		Connection conn = null;
		String sql ="delete from http_request where id=?";
		try {
			conn = DbUtil.getConnection();
			createTableIfNotExist(conn);
			PreparedStatement prtm = conn.prepareStatement(sql);
			prtm.setLong(1, o.getId());
			prtm.executeUpdate();
			prtm.close();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "删除异常："+e.getMessage());

		}finally{
			DbUtil.closeConnection(conn);
		}
		
	}
}
