package com.cloud.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ProcessResult {

	@JsonIgnore
	private String serviceOutput;
	private String endpoint;
	protected Image image;
	protected double startTime;
	protected double endTime;
	protected List<FaceDetection> faces = new ArrayList<FaceDetection>();

	public ProcessResult(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getServiceOutput() {
		return serviceOutput;
	}

	public void setServiceOutput(String serviceOutput) {
		this.serviceOutput = serviceOutput;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}

	public int getFaceCount() {
		return faces.size();
	}

	public List<FaceDetection> getFaces() {
		return faces;
	}

	public void setFaces(List<FaceDetection> faces) {
		this.faces = faces;
	}

	public abstract void process(Map<String, Object> processResult);

}
