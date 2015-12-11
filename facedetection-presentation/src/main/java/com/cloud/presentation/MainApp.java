package com.cloud.presentation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cloud.dto.ProcessResult;
import com.cloud.local.OpenCVProcessor;
import com.cloud.service.Analisis;
import com.cloud.service.Resumen;
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
		Resumen r = new Resumen();
		r = Analisis.analizar(salida);
		
		//Imprime JSON
		try {
			ObjectMapper om = new ObjectMapper();
			System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(salida));

			System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(r));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total caras detectadas: " + r.getCantCarasDetectadas());
		System.out.println("Total caras referenciadas: " + r.getCantCarasReferencia());
		
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
		ocv_p.setfEscalarFace(Double.parseDouble(properties.getPropValue(FDProperties.F_ESCALAR_FACE)));
		ocv_p.setfEscalarEye(Double.parseDouble(properties.getPropValue(FDProperties.F_ESCALAR_EYE)));
		ocv_p.setMinNeighborsFace(Integer.parseInt(properties.getPropValue(FDProperties.MIN_NEIGHBORS_FACE)));
		ocv_p.setMinNeighborsEye(Integer.parseInt(properties.getPropValue(FDProperties.MIN_NEIGHBORS_EYE)));
		ocv_p.setMinPercentSizeFace(Integer.parseInt(properties.getPropValue(FDProperties.MIN_PERCENT_SIZE_FACE)));
		ocv_p.setMaxPercentSizeFace(Integer.parseInt(properties.getPropValue(FDProperties.MAX_PERCENT_SIZE_FACE)));
		ocv_p.setMinPercentSizeEye(Integer.parseInt(properties.getPropValue(FDProperties.MIN_PERCENT_SIZE_EYE)));
		ocv_p.setMaxPercentSizeEye(Integer.parseInt(properties.getPropValue(FDProperties.MAX_PERCENT_SIZE_EYE)));
		ocv_p.setBlurEffect(Integer.parseInt(properties.getPropValue(FDProperties.BLUR_EFFECT)));			
		ocv_p.setShowFaceDetection((properties.getPropValue(FDProperties.SHOW_FACE_OPENCV)).equals("true") || (properties.getPropValue(FDProperties.SHOW_FACE_OPENCV)).equals("TRUE"));
		return ocv_p;
	}
}
