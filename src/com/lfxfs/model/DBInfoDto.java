package com.lfxfs.model;

public class DBInfoDto {
	
	private Integer id;
	
	private String regionName;
	
	private String dbUrl;

	private String dbUser;
	
	private String dbPassWord;

	
	
	public DBInfoDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DBInfoDto(Integer id, String regionName, String dbUrl,
			String dbUser, String dbPassWord) {
		super();
		this.id = id;
		this.regionName = regionName;
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPassWord = dbPassWord;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassWord() {
		return dbPassWord;
	}

	public void setDbPassWord(String dbPassWord) {
		this.dbPassWord = dbPassWord;
	}
	
	

}
