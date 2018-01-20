package com.lfxfs.model;

public class DBDataInfo {

	private Integer dbInfoId;
	
	private Long failNumber;
	private Long total;

	private Long unDoNumber;
	private Long doingNumber;
	private String regionName;
	private String resultCode = "0";	
	private String resultMsg ="巡检成功";	
	private DBInfoDto dbInfo;
	
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public DBDataInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DBDataInfo(Integer dbInfoId, Long failNumber, Long unDoNumber,Long total,
			Long doingNumber) {
		super();
		this.total=total;
		this.dbInfoId = dbInfoId;
		this.failNumber = failNumber;
		this.unDoNumber = unDoNumber;
		this.doingNumber = doingNumber;
	}
	
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Integer getDbInfoId() {
		return dbInfoId;
	}
	public void setDbInfoId(Integer dbInfoId) {
		this.dbInfoId = dbInfoId;
	}
	public Long getFailNumber() {
		return failNumber;
	}
	public void setFailNumber(Long failNumber) {
		this.failNumber = failNumber;
	}
	public Long getUnDoNumber() {
		return unDoNumber;
	}
	public void setUnDoNumber(Long unDoNumber) {
		this.unDoNumber = unDoNumber;
	}
	public Long getDoingNumber() {
		return doingNumber;
	}
	public void setDoingNumber(Long doingNumber) {
		this.doingNumber = doingNumber;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public DBInfoDto getDbInfo() {
		return dbInfo;
	}
	public void setDbInfo(DBInfoDto dbInfo) {
		this.dbInfo = dbInfo;
	}


}
