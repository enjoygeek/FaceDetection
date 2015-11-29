package com.cloud.local;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import com.cloud.common.NativeLibraries;
import com.cloud.dto.Image;
import com.cloud.dto.ProcessResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenCVRunnerService {

	public static final String face_cascade_name = "D:\\openCV\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml";
	public static final String eyes_cascade_name = "D:\\openCV\\opencv\\sources\\data\\haarcascades\\haarcascade_eye.xml";

	public static List<ProcessResult> procesar(Image image) {		
		OpenCVProcessor openCVProcessor = new OpenCVProcessor();

		openCVProcessor.addFaceClassifier(face_cascade_name);
		
		openCVProcessor.addEyesClassifier(eyes_cascade_name);
		openCVProcessor.addEyesClassifier(face_cascade_name);
		return openCVProcessor.process(image);
	}

	// PARA PROBAR
	public static void main(String[] args) {
		List<ProcessResult> procesar = null;
		// http://people.sc.fsu.edu/~jburkardt/data/pgma/lena.ascii.pgm
		// http://estaticos.muyinteresante.es/rcs/articles/2657/hombre-casado.jpg
		// http://www.bodaplanes.com/blog/wp-content/uploads/2012/05/boda-alejandro-sanz.jpg
		// List<ProcessResult> procesar =
		// OpenCVRunnerService.procesar("http://estaticos.muyinteresante.es/rcs/articles/2657/hombre-casado.jpg");
		Image im;
		NumberFormat formatter = new DecimalFormat("0000");
		NativeLibraries.loadLibraries();
		Date comienzo = new java.util.Date();
		for (int i = 0; i <= 100; i++) {
			// System.out.println("Imagen:
			// "+"BioID_"+formatter.format(i)+".pgm");			
			procesar = OpenCVRunnerService
					.procesar(new Image("D:\\datasets\\BioID\\BioID_" + formatter.format(i) + ".pgm", -1, -1));

			ObjectMapper om = new ObjectMapper();
			try {
				if (procesar != null) {
					String writeValueAsString = om.writeValueAsString(procesar);
					System.out.println(writeValueAsString);
				}
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		long fin = new java.util.Date().getTime() - comienzo.getTime();
		System.out.println("Demora total del proceso: " + fin/1000 + " segundos");

	}
}
