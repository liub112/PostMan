package com.lfxfs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lfxfs.model.DBDataInfo;
import com.lfxfs.model.DBInfoDto;
import com.lfxfs.util.DbUtil;

public class DataDao {
	
	private Boolean exists = false;
	
	public int saveDbInfo(Connection conn,List<DBInfoDto> lists) throws Exception {
		String sql ="insert into db_info values(?,?,?,?,?)";
		createTableIfNotExist(conn);
		PreparedStatement prtm = conn.prepareStatement(sql);
		for (int i = 0; i < lists.size(); i++) {
			DBInfoDto dto = lists.get(i);
			prtm.setInt(1, dto.getId());
			prtm.setString(2, dto.getRegionName());
			prtm.setString(3,dto.getDbUrl());
			prtm.setString(4,dto.getDbUser());
			prtm.setString(5,dto.getDbPassWord());
			prtm.addBatch();
		}
		prtm.executeBatch();
//		conn.commit();		
		return 0;
		
	}

	public List<DBInfoDto> queryDbInfo() {
		List<DBInfoDto> lists = new ArrayList<DBInfoDto>();
		Connection conn = null;
		String sql ="select * from db_info";

		try {
			conn = DbUtil.getConnection();
			createTableIfNotExist(conn);
			PreparedStatement prtm = conn.prepareStatement(sql);
			ResultSet rs = prtm.executeQuery();
			while(rs.next()){
				Integer id=	rs.getInt(1);
				String reginName = rs.getString(2);
				String dbUrl = rs.getString(3);
				String dbUser = rs.getString(4);
				String dbPassWord = rs.getString(5);
				DBInfoDto dto = new DBInfoDto(id,reginName,dbUrl,dbUser,dbPassWord);
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
				String ddl ="CREATE TABLE if not exists db_info ("+
				   "id          NUMERIC (8)   PRIMARY KEY"+
				  "                         NOT NULL"+
				  "                          UNIQUE ON CONFLICT REPLACE,"+
		       " region_name VARCHAR (200),"+
				  "  db_url      VARCHAR (200),"+
				  "  db_user     VARCHAR (200),"+
				  "     db_password VARCHAR (200) "+
				")";
				PreparedStatement prtm = conn.prepareStatement(ddl);
				prtm.executeUpdate();				
				exists = true;
				prtm.close();
				
			}
		}

	}

	public DBDataInfo queryStopCountSet(DBInfoDto dto) {
		List<DBDataInfo> lists = new ArrayList<DBDataInfo>();
		Connection conn = null;
		DBDataInfo data = null;
		String sql ="select count(1) \"total\",count(case when status_cd = '1000' then 1 end) \"undo\",count(case when status_cd = '1101' then 1 end) \"doing\",count(case when status_cd = '1300' then 1 end) \"fail\" from " +
		"crm_app_"+dto.getId()+".dis_assemble_record";
		try {
			conn = DbUtil.getConnection(dto.getDbUrl(),dto.getDbUser(),dto.getDbPassWord());
			PreparedStatement prtm = conn.prepareStatement(sql);
			ResultSet rs = prtm.executeQuery();
			if(rs.next()){
				Long total = rs.getLong("total");
				Long undo = rs.getLong("undo");
				Long doing = rs.getLong("doing");
				Long fail = rs.getLong("fail");
				data = new DBDataInfo(dto.getId(),fail,undo,total,doing);
			}
			rs.close();
			prtm.close();
		} catch (Exception e) {
			e.printStackTrace();
			data= new DBDataInfo();
			data.setResultCode("-1");
			data.setResultMsg(e.getMessage());
		}finally{
			DbUtil.closeConnection(conn);
		}
		return data;
		
	}
	
	public int reSend(DBInfoDto dto) {
		List<DBDataInfo> lists = new ArrayList<DBDataInfo>();
		Connection conn = null;
		DBDataInfo data = null;
		String sql ="update crm_app_"+dto.getId()+".dis_assemble_record t set t.status_cd='1000',t.status_date=sysdate  where t.status_cd='1300'";
		try {
			conn = DbUtil.getConnection(dto.getDbUrl(),dto.getDbUser(),dto.getDbPassWord());
			PreparedStatement prtm = conn.prepareStatement(sql);
			int i = prtm.executeUpdate();			
			prtm.close();
			return i;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DbUtil.closeConnection(conn);
		}
		return 0;
		
	}
}
