package com.cloud.service.opencv;

import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;
import org.opencv.objdetect.CascadeClassifier;

import com.cloud.dto.FaceDetection;
import com.cloud.dto.ProcessResult;
import com.cloud.remote.OpenCVProcessResult;

public class OpenCVProcessor {

	private final String BASE_PATH = "D:\\datasets\\uploaded\\";

	private List<String> clasificadores;

	public OpenCVProcessor() {
		this.clasificadores = new ArrayList<String>();
	}

	public List<String> getClasificadores() {
		return clasificadores;
	}

	public void addClasificador(String clasificador) {
		this.clasificadores.add(clasificador);
	}

	private String downloadImage(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			InputStream in = new BufferedInputStream(url.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();

			Random random = new Random();
			String imageFormat = imageUrl.substring(imageUrl.lastIndexOf("."));
			String filename = BASE_PATH + random.nextLong() + "." + imageFormat;
			FileOutputStream fos = new FileOutputStream(filename);
			fos.write(response);
			fos.close();
			return filename;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public void loadLibrary(){
		try{
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		}catch(Exception e){
			//NADA
		}
	}

	public List<ProcessResult> process(String imagePath) {
		String fileName = this.downloadImage(imagePath);
		//TODO devolver un processresult vacio
		if(fileName == null || fileName.isEmpty()) return null;
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		loadLibrary();
		
		List<ProcessResult> salida = new ArrayList<ProcessResult>();
		for (int j = 0; j < clasificadores.size(); j++) {
			
			// Directorio existe
			String clasificador = clasificadores.get(j);
			int detectados = 0;

			Mat image = Imgcodecs.imread(fileName, 1);			
			// Mat imagenOrigen = Imgcodecs.imread("Rostro.png",1);
			// Imshow im = new Imshow("Ejemplo");
			// im.showImage(imagenOrigen);

			// ***Efectos sobre la imagen*******/
			/******* Blur *******/
			// Imgproc.GaussianBlur(imagenOrigen, imagenDestino, new
			// Size(45,45),0);
			// im.showImage(imagenDestino);

			/******* Gray *******/
			// Imgproc.cvtColor(imagenOrigen, imagenDestino,
			// Imgproc.COLOR_BGR2GRAY); //Gris
			// Imgproc.blur(imagenDestino, imagenDestino, new Size(9, 9));
			// im.showImage(imagenDestino);

			/******* Canny *******/
			// Imgproc.Canny(imagenOrigen, imagenDestino, 10, 100);
			// im.showImage(imagenDestino);

			/******* Threshold *******/
			// Imgproc.threshold(imagenOrigen, imagenDestino, 50, 150, 1);
			// im.showImage(imagenDestino);

			/***************** FaceTracking ************************************/
			// Create a face detector from the cascade file in the resources

			CascadeClassifier faceDetector = new CascadeClassifier(clasificador);
			// Detect faces in the image.

			// MatOfRect is a special container class for Rect.
			MatOfRect faceDetections = new MatOfRect();
			faceDetector.detectMultiScale(image, faceDetections);

			// System.out.println(String.format("Detected %s faces",
			// faceDetections.toArray().length));

			// Draw a bounding box around each face.
			List<FaceDetection> faces = new ArrayList<FaceDetection>();
			for (Rect rect : faceDetections.toArray()) {
				FaceDetection faceDetection = new FaceDetection();
			
				faceDetection.setBoundingBox(new Rectangle2D.Double(rect.x,rect.y,rect.width,rect.height));
				faces.add(faceDetection);
				
				detectados++;
			}
			
			Map<String, Object> resultado = new HashMap<String,Object>();
			resultado.put("faces", faces);			
			resultado.put("path", fileName);
			resultado.put("width", image.width());
			resultado.put("height", image.height());
			resultado.put("size", new File(fileName).length());
			OpenCVProcessResult openCVProcessResult = new OpenCVProcessResult("OpenCV - "+clasificador);
			openCVProcessResult.process(resultado);
			salida.add(openCVProcessResult);
			
			// if (faceDetections.toArray().length >0){
			// im.showImage(imagenDestino);
			// }
			// System.out.println("Detectados: "+detectados);
			System.out.println("Clasificador: "+clasificador+" - Detectados: "+detectados);
		}		
		//Devolver processResult correcto
		return salida;
	}

}