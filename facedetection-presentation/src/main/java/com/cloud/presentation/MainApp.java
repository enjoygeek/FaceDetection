package com.cloud.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cloud.dto.ProcessResult;
import com.cloud.service.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainApp {

	protected static Logger logger = Logger.getGlobal();

	public static void main(String[] args) {
		FDProperties properties = FDProperties.getInstance();
		String dataset = properties.getPropValue(FDProperties.DATASET);
		String faceCascade = properties
				.getPropValue(FDProperties.FACE_CASCADE_URL);
		String eyeCascade = properties
				.getPropValue(FDProperties.EYE_CASCADE_URL);

		List<ProcessResult> salida = new ArrayList<ProcessResult>();
		try {
			TestService ts = new TestService(dataset, faceCascade, eyeCascade);
			for (ProcessResult pr : ts.test()) {
				salida.add(pr);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		
		//Imprime JSON
		try {
			ObjectMapper om = new ObjectMapper();
			System.out.println(om.writeValueAsString(salida));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
