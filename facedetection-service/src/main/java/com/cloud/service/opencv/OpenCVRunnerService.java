package com.cloud.service.opencv;

import java.util.List;

import com.cloud.dto.ProcessResult;

public class OpenCVRunnerService {
	
	public static List<ProcessResult> procesar(String image){
		
		OpenCVProcessor openCVProcessor = new OpenCVProcessor();
		
		 // clasificadores.add("haarcascade_eye.xml");
		 // clasificadores.add("lbpcascade_frontalface.xml");
		 openCVProcessor.addClasificador("D:\\openCV\\clasificadores\\haarcascade_eye_tree_eyeglasses.xml");
		 return openCVProcessor.process(image);
		 
	}
	
	//PRA PROBAR
	public static void main(String[] args){
		OpenCVRunnerService.procesar("http://www.weddingcostarica.com/images/pack1.jpg");
	}
}

