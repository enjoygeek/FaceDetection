package com.cloud.presentation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cloud.dto.Image;
import com.cloud.dto.ProcessResult;
import com.cloud.local.OpenCVProcessor;
import com.cloud.remote.FacePlusPlus;
import com.cloud.remote.FaceRect;
import com.cloud.remote.SkyBiometry;
import com.cloud.service.Analisis;
import com.cloud.service.Resumen;
import com.cloud.service.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainApp {

	protected static Logger logger = Logger.getGlobal();
	private static String urlResultados;

	public static void main(String[] args) {
		FDProperties properties = FDProperties.getInstance();
		String dataset = properties.getPropValue(FDProperties.REMOTE_DATASET);
		String imageService = properties.getPropValue(FDProperties.REMOTE_IMAGE);
		urlResultados = properties.getPropValue(FDProperties.RESULTADOS);
		Image.BASE_PATH_TO_DOWNLOAD = properties.getPropValue(FDProperties.DOWNLOAD_PATH);

		List<ProcessResult> salida = new ArrayList<ProcessResult>();
		Double timeTotal = 0.0;
		Double timeInternalTotal = 0.0;
		try {
			TestService ts = new TestService(dataset, imageService);
			// Agrego los servicios disponibles
			// OpenCVProcessor ocv_p = initOpenCV_Parameters(properties);
			// ts.addService(ocv_p);
			// FaceRect fRect = initFaceRect_Parameters(properties);
			// ts.addService(fRect);

			//SkyBiometry sBiometry = initSkyBiometry_Parameters(properties);
			//ts.addService(sBiometry);

			 FacePlusPlus facePlusPlus = initFacePlusPlus_Parameters(properties);
			 ts.addService(facePlusPlus);

			for (ProcessResult pr : ts.test()) {
				salida.add(pr);	
				timeTotal += pr.getElapsedTime();
				timeInternalTotal += pr.getInternalTime();
				
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	
		Resumen r = new Resumen();
		r = Analisis.analizar(salida);
		r.procesarDistancias(salida);

		// Imprime JSON
		try {

			ObjectMapper om = new ObjectMapper();

			escribirArchivo(om.writerWithDefaultPrettyPrinter().writeValueAsString(salida));
			// System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(salida));

			// System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(r));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Blur: " + properties.getPropValue(FDProperties.BLUR_EFFECT));
		System.out.println("Total caras detectadas: " + r.getCantCarasDetectadas());
		System.out.println("Total caras referenciadas: " + r.getCantCarasReferencia());
		System.out.println("Porcenta de efectividad: " + r.getPorcentaje());

		System.out.println("Total puntos detectados: " + r.getPuntosDetectados());

		System.out.println("Distancia total: " + round(r.getDistanciaTotal(), 2));
		System.out.println("Promedio de distancias: " + round(r.getPromedioDistancia(), 2));
		System.out.println("Varianza de distancias: " + round(r.getVarianzaDistancia(), 2));
		System.out.println("Desviación Estandar de distancias: " + round(r.getDesviacionDistancia(), 2));
		System.out.println("Tiempo total en Segundos: " + round(timeTotal, 0)/1000);
		System.out.println("Promedio de Tiempo por Cara Detectada: " + (round(timeTotal, 0)/1000)/r.getCantCarasReferencia());
		System.out.println("Tiempo total Interno en Segundos: " + round(timeInternalTotal, 0)/1000);
		System.out.println("Promedio de Tiempo Interno por Cara Detectada: " + (round(timeInternalTotal, 0)/1000)/r.getCantCarasDetectadas());
		
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	private static void escribirArchivo(String texto) {
		String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		System.out.println(urlResultados + File.separator + timeLog + ".txt");
		File logFile = new File(urlResultados + File.separator + timeLog + ".txt");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(logFile));
			writer.write(texto);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	private static FacePlusPlus initFacePlusPlus_Parameters(FDProperties properties) {
		String serviceUrl = properties.getPropValue(FDProperties.FACEPLUSPLUS_SERVICEURL);
		String xMashapeKey = properties.getPropValue(FDProperties.XMASHAPEKEY);
		String attributes = FacePlusPlus.DEFAULT_ATTRIBUTES;

		FacePlusPlus facePlusPlus = new FacePlusPlus(serviceUrl, xMashapeKey, attributes);
		return facePlusPlus;
	}

	private static SkyBiometry initSkyBiometry_Parameters(FDProperties properties) {
		String serviceUrl = properties.getPropValue(FDProperties.SKYBIOMETRY_SERVICEURL);
		String xMashapeKey = properties.getPropValue(FDProperties.XMASHAPEKEY);
		String api_key = properties.getPropValue(FDProperties.SKYBIOMETRY_API_KEY);
		String api_secret = properties.getPropValue(FDProperties.SKYBIOMETRY_API_SECRET);
		String attributes = SkyBiometry.DEFAULT_ATTRIBUTES;
		String detector = SkyBiometry.DEFAULT_DETECTOR;
		SkyBiometry skyBiometry = new SkyBiometry(serviceUrl, xMashapeKey, api_key, api_secret, attributes, detector);
		return skyBiometry;
	}

	private static FaceRect initFaceRect_Parameters(FDProperties properties) {
		String serviceUrl = properties.getPropValue(FDProperties.FACERECT_SERVICEURL);
		String xMashapeKey = properties.getPropValue(FDProperties.XMASHAPEKEY);
		String attributes = FaceRect.DEFAULT_ATTRIBUTES;
		FaceRect fRect = new FaceRect(serviceUrl, xMashapeKey, attributes);
		return fRect;
	}

	private static OpenCVProcessor initOpenCV_Parameters(FDProperties properties) {
		String faceCascade = properties.getPropValue(FDProperties.FACE_CASCADE_URL);
		String eyeCascade = properties.getPropValue(FDProperties.EYE_CASCADE_URL);
		urlResultados = properties.getPropValue(FDProperties.RESULTADOS);
		if (!new File(faceCascade).exists()) {
			throw new ExceptionInInitializerError("Face-Classifier not found");
		}
		if (!new File(eyeCascade).exists()) {
			throw new ExceptionInInitializerError("Eye-Classifier not found");
		}

		OpenCVProcessor ocv_p = new OpenCVProcessor(faceCascade, eyeCascade);
		ocv_p.setfEscalarFace(Double.parseDouble(properties.getPropValue(FDProperties.F_ESCALAR_FACE)));
		ocv_p.setfEscalarEye(Double.parseDouble(properties.getPropValue(FDProperties.F_ESCALAR_EYE)));
		ocv_p.setMinNeighborsFace(Integer.parseInt(properties.getPropValue(FDProperties.MIN_NEIGHBORS_FACE)));
		ocv_p.setMinNeighborsEye(Integer.parseInt(properties.getPropValue(FDProperties.MIN_NEIGHBORS_EYE)));
		ocv_p.setMinPercentSizeFace(Integer.parseInt(properties.getPropValue(FDProperties.MIN_PERCENT_SIZE_FACE)));
		ocv_p.setMaxPercentSizeFace(Integer.parseInt(properties.getPropValue(FDProperties.MAX_PERCENT_SIZE_FACE)));
		ocv_p.setMinPercentSizeEye(Integer.parseInt(properties.getPropValue(FDProperties.MIN_PERCENT_SIZE_EYE)));
		ocv_p.setMaxPercentSizeEye(Integer.parseInt(properties.getPropValue(FDProperties.MAX_PERCENT_SIZE_EYE)));
		ocv_p.setBlurEffect(Integer.parseInt(properties.getPropValue(FDProperties.BLUR_EFFECT)));
		ocv_p.setShowFaceDetection((properties.getPropValue(FDProperties.SHOW_FACE_OPENCV)).equals("true")
				|| (properties.getPropValue(FDProperties.SHOW_FACE_OPENCV)).equals("TRUE"));
		return ocv_p;
	}
}
