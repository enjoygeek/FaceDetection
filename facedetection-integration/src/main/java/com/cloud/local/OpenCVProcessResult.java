package com.cloud.local;

import java.util.List;
import java.util.Map;

import com.cloud.dto.FaceDetection;
import com.cloud.dto.Image;
import com.cloud.dto.ProcessResult;

public class OpenCVProcessResult extends ProcessResult {
	
	public OpenCVProcessResult(String endpoint) {
		super(endpoint);
		
	}


	@Override
	public void process(Map<String, Object> processResult) {
		String uri = (String)processResult.get("path");
		int width = (int)processResult.get("width");
		int height = (int)processResult.get("height");
		long fileSize = (long)processResult.get("size");
		List<FaceDetection> faces=	(List<FaceDetection>)processResult.get("faces");					
		this.setFaces(faces);
		this.image = new Image(uri, width, height, fileSize);

	}

}
