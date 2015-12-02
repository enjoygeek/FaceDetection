package com.cloud.local;

import java.util.Map;

import com.cloud.dto.Image;
import com.cloud.dto.ProcessResult;

public class OpenCVProcessResult extends ProcessResult {
	
	public OpenCVProcessResult(String endpoint) {
		super(endpoint);		
	}

	@Override
	public void process(Map<String, Object> processResult) {
		image = (Image)processResult.get("image");					
		this.endTime = (long)processResult.get("elapsedTime");
		//this.image = new Image(uri, uri.substring(uri.lastIndexOf('.'),uri.length()),width, height, fileSize);
	}

}
