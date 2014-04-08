package com.example.moleurlshortener.DAO;

public class UrlObject {

	private String originalUrl;
	private String createdUrl;

	public UrlObject() {
		setOriginalUrl("");
		setCreatedUrl("");
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getCreatedUrl() {
		return createdUrl;
	}

	public void setCreatedUrl(String createdUrl) {
		this.createdUrl = createdUrl;
	}

}
