package com.cloud.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProcessResult {
	
	@JsonIgnore
	private String serviceOutput;
	private String endpoint;
	protected double time;
	protected List<FaceDetection> faces = new ArrayList<FaceDetection>();

	public ProcessResult(String endpoint, String result){
		this.endpoint = endpoint;
		this.serviceOutput = result;
	}
	
	public ProcessResult(String endpoint, Map<String, Object> result) {
		this.endpoint = endpoint;
		this.serviceOutput = result.toString();
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getServiceOutput() {
		return serviceOutput;
	}

	public void setServiceOutput(String serviceOutput) {
		this.serviceOutput = serviceOutput;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
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

}
