package com.lfxfs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lfxfs.model.DBInfoDto;
import com.lfxfs.model.HttpRequest;
import com.lfxfs.util.DbUtil;

public class httpDao {
	private Boolean exists = false;
	
	public void saveReqInfo(HttpRequest req) {
		Connection conn =null;
		try {			
			conn = DbUtil.getConnection();
			String sql ="insert into http_request values(?,?,?,?,?)";
			createTableIfNotExist(conn);
			PreparedStatement prtm = conn.prepareStatement(sql);
			prtm.setString(1, req.getUrl());
			prtm.setString(2,req.getReqText());
			prtm.setString(3,req.getRspText());
			prtm.setString(4,req.getHttpHeader());
			prtm.setTimestamp(5, new Timestamp(new Date().getTime()));
			prtm.executeUpdate();
			prtm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DbUtil.closeConnection(conn);
		}
		
	}

	public List<HttpRequest> queryReqInfo() {
		List<HttpRequest> lists = new ArrayList<HttpRequest>();
		Connection conn = null;
		String sql ="select * from http_request order by create_date desc";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			conn = DbUtil.getConnection();
			createTableIfNotExist(conn);
			PreparedStatement prtm = conn.prepareStatement(sql);
			ResultSet rs = prtm.executeQuery();
			while(rs.next()){
				String url=	rs.getString(1);
				String reqText = rs.getString(2);
				String rspText = rs.getString(3);
				String htttpHeader = rs.getString(4);
				Timestamp httpDate = rs.getTimestamp(5);	
				HttpRequest dto = new HttpRequest(url,htttpHeader,reqText,rspText,sdf.format(new Date(httpDate.getTime())));
				lists.add(dto);
			}
			rs.close();
			prtm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DbUtil.closeConnection(conn);
		}
		return lists;
		
	}
	
	private void createTableIfNotExist(Connection conn) throws Exception{
		if(!exists){
			synchronized(exists){
				String ddl ="CREATE TABLE if not exists http_request ("+
				   "url         VARCHAR (200),"+
				  " req_text    VARCHAR (4000),"+
				  "rsp_text    VARCHAR (4000),"+
				  " http_header VARCHAR (800),"+
				  " create_date DATE    DEFAULT sysdate"+
				")";
				PreparedStatement prtm = conn.prepareStatement(ddl);
				prtm.executeUpdate();				
				exists = true;
				prtm.close();
				
			}
		}

	}
}
