package com.lfxfs.model;

public class HttpRequest {
	private Long id;
	
	private String url;
	
	private String httpHeader;
	
	private String reqText;
	
	private String rspText;
	
	private String reqDate;
	//耗时
	private Long cost;

	
	
	@Override
	public String toString() {
		return url;
	}
	
	

	public Long getCost() {
		return cost;
	}



	public void setCost(Long cost) {
		this.cost = cost;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public String getReqDate() {
		return reqDate;
	}

	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHttpHeader() {
		return httpHeader;
	}

	public void setHttpHeader(String httpHeader) {
		this.httpHeader = httpHeader;
	}

	public String getReqText() {
		return reqText;
	}

	public void setReqText(String reqText) {
		this.reqText = reqText;
	}

	public String getRspText() {
		return rspText;
	}

	public void setRspText(String rspText) {
		this.rspText = rspText;
	}

	public HttpRequest(Long id,String url, String httpHeader, String reqText,
			String rspText,String date,Long cost) {
		super();
		this.id=id;
		this.url = url;
		this.httpHeader = httpHeader;
		this.reqText = reqText;
		this.rspText = rspText;
		this.reqDate = date;
		this.cost = cost;
	}

	public HttpRequest() {
		super();
	}
	
	

}
