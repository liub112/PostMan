package com.lfxfs.model;

public class HttpKeep {
	
	private Long id;
	
	private String httpName;
	
	private String httpUrl;
	
	private String reqText;

	public HttpKeep() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HttpKeep(String httpName, String httpUrl, String reqText) {
		super();
		this.httpName = httpName;
		this.httpUrl = httpUrl;
		this.reqText = reqText;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHttpName() {
		return httpName;
	}

	public void setHttpName(String httpName) {
		this.httpName = httpName;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public String getReqText() {
		return reqText;
	}

	public void setReqText(String reqText) {
		this.reqText = reqText;
	}

	@Override
	public String toString() {
		return httpName;
	}
	
	

}
