package com.lfxfs.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
	
//	private static String dbUrl ="jdbc:sqlite:db/mySqlLite.db";
	
	private static String dbUser = "";
	
	private static String dbPassWord ="";
	
	private static String jdbcDriver ="org.sqlite.JDBC";
	
	public static Connection getConnection() throws Exception {
		String path = System.getProperties().getProperty("user.home");
		
		String dbUrl = "jdbc:sqlite:"+path+"/mySqlLite.db";
		Class.forName(jdbcDriver);
		Connection conn = DriverManager.getConnection(dbUrl);
		return conn;
	}
	
	public static Connection getConnection(String dbUrl,String dbUser,String dbPassWord) throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection(dbUrl,dbUser,dbPassWord);
		return conn;
	}
	
	public static void  closeConnection(Connection conn) {
		if(conn!=null){
			try {
				conn.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			Connection conn = getConnection();
			System.out.println("数据库连接成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
