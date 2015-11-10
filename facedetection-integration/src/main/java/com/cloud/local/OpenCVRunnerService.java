package com.cloud.local;

import java.util.List;

import com.cloud.dto.ProcessResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenCVRunnerService {
	
	public static List<ProcessResult> procesar(String image){
		
		OpenCVProcessor openCVProcessor = new OpenCVProcessor();
		
		 // clasificadores.add("haarcascade_eye.xml");
		 // clasificadores.add("lbpcascade_frontalface.xml");
		 openCVProcessor.addClasificador("D:\\openCV\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_default.xml");
		 return openCVProcessor.process(image);
		 
	}
	
	//PRA PROBAR
	public static void main(String[] args){
		List<ProcessResult> procesar = OpenCVRunnerService.procesar("http://www.weddingcostarica.com/images/pack1.jpg");
		ObjectMapper om = new ObjectMapper();
		try {
			String writeValueAsString = om.writeValueAsString(procesar);
			System.out.println(writeValueAsString);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

