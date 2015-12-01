package com.cloud.presentation;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cloud.dto.ProcessResult;
import com.cloud.service.TestService;

public class MainApp {

	protected static Logger logger = Logger.getGlobal();
	
	public static void main(String[] args) {
		FDProperties properties = FDProperties.getInstance();
		String dataset = properties.getPropValue(FDProperties.DATASET);
		String faceCascade = properties.getPropValue(FDProperties.FACE_CASCADE_URL);
		String eyeCascade = properties.getPropValue(FDProperties.EYE_CASCADE_URL);
		try {
			TestService ts = new TestService(dataset, faceCascade, eyeCascade);
			for (ProcessResult pr : ts.test()) {
				System.out.println(pr);
			}
		} catch (ExceptionInInitializerError e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
	}
}
