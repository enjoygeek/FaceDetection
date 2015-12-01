package com.cloud.presentation;

import com.cloud.dto.ProcessResult;
import com.cloud.service.TestService;

public class MainApp {

	public static void main(String[] args) {		
		FDProperties properties = FDProperties.getInstance();
		String dataset = properties.getPropValue(FDProperties.DATASET);
		String faceCascade = properties.getPropValue(FDProperties.FACE_CASCADE_URL);
		String eyeCascade = properties.getPropValue(FDProperties.EYE_CASCADE_URL);
		TestService ts = new TestService(dataset, faceCascade,eyeCascade);
		for (ProcessResult pr : ts.test()) {
			System.out.println(pr);
		}
	}
}
