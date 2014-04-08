package com.example.moleurlshortener.DAO;

import java.io.Serializable;

public class UrlObject implements Serializable {

	private static final long serialVersionUID = -7060210544600464481L;

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
