package com.cloud.dto;

public class Image {
	
	private String uri;
	private int width;
	private int height;
	private long fileSize;
	
	public Image(){};

	public Image(String uri, int width, int height, long fileSize) {
		super();
		this.uri = uri;
		this.width = width;
		this.height = height;
		this.fileSize = fileSize;
	}

	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public double getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
