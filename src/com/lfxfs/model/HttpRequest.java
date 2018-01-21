package com.lfxfs.model;

public class HttpRequest {
	private Long id;
	
	private String url;
	
	private String httpHeader;
	
	private String reqText;
	
	private String rspText;
	
	private String reqDate;

	
	
	@Override
	public String toString() {
		return "HttpRequest [url=" + url + "],reqDate=" + reqDate + "]" ;
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
			String rspText,String date) {
		super();
		this.id=id;
		this.url = url;
		this.httpHeader = httpHeader;
		this.reqText = reqText;
		this.rspText = rspText;
		this.reqDate = date;
	}

	public HttpRequest() {
		super();
	}
	
	

}
