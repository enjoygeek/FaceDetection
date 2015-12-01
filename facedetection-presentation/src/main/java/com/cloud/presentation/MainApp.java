package com.cloud.presentation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cloud.dto.ProcessResult;
import com.cloud.local.OpenCVProcessor;
import com.cloud.remote.FacePlusPlus;
import com.cloud.service.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainApp {

	protected static Logger logger = Logger.getGlobal();

	public static void main(String[] args) {
		FDProperties properties = FDProperties.getInstance();
		String dataset = properties.getPropValue(FDProperties.DATASET);
		
		
		List<ProcessResult> salida = new ArrayList<ProcessResult>();
		try {			
			OpenCVProcessor ocv_p = initOpenCV_Parameters(properties);
			TestService ts = new TestService(dataset);
			//Agrego los servicios disponibles
			
			ts.addService(ocv_p);
			//ts.addService(new FacePlusPlus(FacePlusPlus.DEFAULT_ATTRIBUTES));
			
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

	private static OpenCVProcessor initOpenCV_Parameters(FDProperties properties) {
		String faceCascade = properties.getPropValue(FDProperties.FACE_CASCADE_URL);
		String eyeCascade = properties.getPropValue(FDProperties.EYE_CASCADE_URL);
		if (!new File(faceCascade).exists()){			
			throw new ExceptionInInitializerError("Face-Classifier not found");			
		}
		if (!new File(eyeCascade).exists()){
			throw new ExceptionInInitializerError("Eye-Classifier not found");
		}
		
		OpenCVProcessor ocv_p = new OpenCVProcessor(faceCascade,eyeCascade); 		
		ocv_p.setfEscalar(Double.parseDouble(properties.getPropValue(FDProperties.F_ESCALAR)));
		ocv_p.setMinNeighbors(Integer.parseInt(properties.getPropValue(FDProperties.MIN_NEIGHBORS)));
		ocv_p.setMinPercentSizeFace(Integer.parseInt(properties.getPropValue(FDProperties.MIN_PERCENT_SIZE_FACE)));
		ocv_p.setMaxPercentSizeFace(Integer.parseInt(properties.getPropValue(FDProperties.MAX_PERCENT_SIZE_FACE)));
		ocv_p.setMinPercentSizeEye(Integer.parseInt(properties.getPropValue(FDProperties.MIN_PERCENT_SIZE_EYE)));
		ocv_p.setMaxPercentSizeEye(Integer.parseInt(properties.getPropValue(FDProperties.MAX_PERCENT_SIZE_EYE)));
		return ocv_p;
	}
}
