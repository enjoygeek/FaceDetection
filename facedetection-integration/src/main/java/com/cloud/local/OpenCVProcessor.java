package com.cloud.local;

import java.awt.geom.Rectangle2D;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import com.cloud.common.IService;
import com.cloud.dto.Coordinate2D;
import com.cloud.dto.FaceDetection;
import com.cloud.dto.Image;
import com.cloud.dto.ProcessResult;

public class OpenCVProcessor implements IService {

	private CascadeClassifier face_cascade;
	private CascadeClassifier eyes_cascade;
	private double fEscalarFace; // Valores mas altos significan menos
									// precisi�n.
	private double fEscalarEye; // Valores mas altos significan menos precisi�n.
	private int minNeighborsFace;// Valores mas altos significan menos precisi�n
									// pero mas fiabilidad
	private int minNeighborsEye;// Valores mas altos significan menos precisi�n
								// pero mas fiabilidad
	private int minPercentSizeFace;
	private int maxPercentSizeFace;
	private int minPercentSizeEye;
	private int maxPercentSizeEye;
	private boolean showFaceDetection;	
	private int blurEffect;

	public OpenCVProcessor(String face_cascade_url, String eyes_cascade_url) {
		// //// Defino los clasificadores//////
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println(Paths.get(face_cascade_url).toString());
		setFaceClassifier(new CascadeClassifier(Paths.get(face_cascade_url).toString()));
		setEyesClassifier(new CascadeClassifier(Paths.get(eyes_cascade_url).toString()));
	}

	public void setFaceClassifier(CascadeClassifier clasificador) {
		this.face_cascade = clasificador;
	}

	public void setEyesClassifier(CascadeClassifier clasificador) {
		this.eyes_cascade = clasificador;
	}

	public CascadeClassifier getFaceClassifier() {
		return face_cascade;
	}

	public CascadeClassifier getEyesClassifier() {
		return eyes_cascade;
	}

	private ProcessResult process(Image image) {
		// TODO devolver un processresult vacio
		if (image == null)
			return null;

		Coordinate2D leftEye, rightEye, nose, mouth;

		MatOfRect faceDetections = new MatOfRect();
		MatOfRect eyeDetections = new MatOfRect();
		Rect[] sortEyes;
		FaceDetection faceDetection;
		Rect rect_face;

		java.util.Date comienzo = new java.util.Date();
		// /////////////////////////////
		// Comienzo detecci�n de rostro
		// /////////////////////////////
		getFaceClassifier().detectMultiScale(image.getMatImage(), faceDetections, fEscalarFace, minNeighborsFace, 0,
				new Size(image.getWidth() * minPercentSizeFace / 100, image.getHeight() * minPercentSizeFace / 100),
				new Size(image.getWidth() * maxPercentSizeFace / 100, image.getHeight() * maxPercentSizeFace / 100));

		List<FaceDetection> faces = new ArrayList<FaceDetection>();
		for (int r = 0; r < faceDetections.toArray().length; r++) {
			rect_face = faceDetections.toArray()[r];
			faceDetection = new FaceDetection();
			faceDetection.setBoundingBox(
					new Rectangle2D.Double(rect_face.x, rect_face.y, rect_face.width, rect_face.height));
			// /////////////////////////////
			// Comienzo detecci�n de ojos
			// /////////////////////////////
			leftEye = rightEye = nose = mouth = null; // Reinicio los puntos del
														// rostro anterior

			Mat faceROI = image.getMatImage().submat(faceDetections.toArray()[r]);
			Mat faceROI_Effect = setEffect(faceROI);

			getEyesClassifier().detectMultiScale(faceROI_Effect, eyeDetections, fEscalarEye, minNeighborsEye, 0,
					new Size(image.getWidth() * minPercentSizeEye / 100, image.getHeight() * minPercentSizeEye / 100),
					new Size(image.getWidth() * maxPercentSizeEye / 100, image.getHeight() * maxPercentSizeEye / 100));			
			if (eyeDetections.toArray().length > 1) {
				sortEyes = sortRect(eyeDetections.toArray());
				rightEye = new Coordinate2D(sortEyes[0].x + rect_face.x, sortEyes[0].y + rect_face.y);
				leftEye = new Coordinate2D(sortEyes[sortEyes.length - 1].x + rect_face.x,
						sortEyes[sortEyes.length - 1].y + rect_face.y);
				// El ultimo en el eje x es siempre el ojo izquierdo de la
				// persona

				if (sortEyes.length == 3) {
					// Se detecto la nariz
					nose = new Coordinate2D(sortEyes[1].x + rect_face.x, sortEyes[1].y + rect_face.y);
				} else if (sortEyes.length == 4) {
					// Se detecto nariz y boca
					if (sortEyes[1].y < sortEyes[2].y) {
						nose = new Coordinate2D(sortEyes[1].x + rect_face.x, sortEyes[1].y + rect_face.y);
						mouth = new Coordinate2D(sortEyes[2].x + rect_face.x, sortEyes[2].y + rect_face.y);
					} else {
						mouth = new Coordinate2D(sortEyes[1].x + rect_face.x, sortEyes[1].y + rect_face.y);
						nose = new Coordinate2D(sortEyes[2].x + rect_face.x, sortEyes[2].y + rect_face.y);
					}
				}
			}
			if (showFaceDetection){
				for (int eyes = 0; eyes < eyeDetections.toArray().length; eyes++) {
					// Dibujar los ojos
					int radius = 5;
					Point center_Eye = new Point(eyeDetections.toArray()[eyes].x, eyeDetections.toArray()[eyes].y);
					org.opencv.imgproc.Imgproc.circle(faceROI, center_Eye, radius,
							new Scalar(Math.min(5 * eyes, 255), Math.min(30 * eyes, 255), Math.min(75 * eyes, 255)), 5, 5,
							0);
				} // Mostrar la cara analizada
				Imshow im = new Imshow("Face number: " + r);
				im.showImage(faceROI);
			}

			faceDetection.setLeftEye(leftEye);
			faceDetection.setRightEye(rightEye);
			faceDetection.setNose(nose);
			faceDetection.setMouth(mouth);
			faces.add(faceDetection);
		}

		image.setDetections(faces);
		long fin = new java.util.Date().getTime() - comienzo.getTime();
		Map<String, Object> resultado = new HashMap<String, Object>();
		resultado.put("elapsedTime", fin);
		resultado.put("image", image);
		String endpoint = "OpenCV - " + face_cascade.toString();
		OpenCVProcessResult openCVProcessResult = new OpenCVProcessResult(endpoint);
		openCVProcessResult.process(resultado);

		// Devolver processResult correcto
		return openCVProcessResult;
	}

	/**
	 * Ordena por el eje X, por lo cual si hay 3, el elemento del medio es la
	 * nariz y si hay 4, el segundo y el tercero son nariz y boca
	 * 
	 * @param eyeDetections
	 * @return
	 */
	private Rect[] sortRect(Rect[] eyeDetections) {
		Rect[] eyeDetectionSorted = eyeDetections;
		Rect temp;
		for (int i = 0; i < eyeDetectionSorted.length; i++) {
			for (int j = 0; j < eyeDetectionSorted.length - 1; j++) {
				if (eyeDetectionSorted[j].x > eyeDetectionSorted[j + 1].x) {
					temp = eyeDetectionSorted[j + 1];
					eyeDetectionSorted[j + 1] = eyeDetectionSorted[j];
					eyeDetectionSorted[j] = temp;
				}
			}
		}
		return eyeDetectionSorted;
	}

	private Mat setEffect(Mat imagenOrigen) {
		Mat imagenDestino = new Mat();
		// ***Efectos sobre la imagen*******/
		/******* Blur *******/
		// Imgproc.GaussianBlur(imagenOrigen, imagenDestino, new Size(9,9),0);

		/******* Gray *******/
		// Imgproc.cvtColor(imagenOrigen, imagenDestino,
		// Imgproc.COLOR_BGR2GRAY);

		/******* BLUR *******/
		if (getBlurEffect() > 0){
			//System.out.println("Bluur: "+getBlurEffect());
			Imgproc.blur(imagenOrigen, imagenDestino, new Size(getBlurEffect(), getBlurEffect()));
		}
		else
			imagenDestino = imagenOrigen;
		/******* Canny *******/
		// Imgproc.Canny(imagenOrigen, imagenDestino, 10, 100);

		/******* Threshold *******/
		// Imgproc.threshold(imagenOrigen, imagenDestino, 80, 150, 1);
		//System.out.println("ShowFaceDetection: "+showFaceDetection);
		if (showFaceDetection){
			Imshow im = new Imshow("Face with effect");
			im.showImage(imagenDestino);
		}
		return imagenDestino;
	}

	@Override
	public ProcessResult run(Image image) {
		return process(image);
	}

	public int getMinPercentSizeFace() {
		return minPercentSizeFace;
	}

	public void setMinPercentSizeFace(int minPercentSizeFace) {
		this.minPercentSizeFace = minPercentSizeFace;
	}

	public int getMaxPercentSizeFace() {
		return maxPercentSizeFace;
	}

	public void setMaxPercentSizeFace(int maxPercentSizeFace) {
		this.maxPercentSizeFace = maxPercentSizeFace;
	}

	public int getMinPercentSizeEye() {
		return minPercentSizeEye;
	}

	public void setMinPercentSizeEye(int minPercentSizeEye) {
		this.minPercentSizeEye = minPercentSizeEye;
	}

	public int getMaxPercentSizeEye() {
		return maxPercentSizeEye;
	}

	public void setMaxPercentSizeEye(int maxPercentSizeEye) {
		this.maxPercentSizeEye = maxPercentSizeEye;
	}

	public double getfEscalarFace() {
		return fEscalarFace;
	}

	public void setfEscalarFace(double fEscalarFace) {
		this.fEscalarFace = fEscalarFace;
	}

	public double getfEscalarEye() {
		return fEscalarEye;
	}

	public void setfEscalarEye(double fEscalarEye) {
		this.fEscalarEye = fEscalarEye;
	}

	public int getMinNeighborsFace() {
		return minNeighborsFace;
	}

	public void setMinNeighborsFace(int minNeighborsFace) {
		this.minNeighborsFace = minNeighborsFace;
	}

	public int getMinNeighborsEye() {
		return minNeighborsEye;
	}

	public void setMinNeighborsEye(int minNeighborsEye) {
		this.minNeighborsEye = minNeighborsEye;
	}
	
	public boolean isShowFaceDetection() {
		return showFaceDetection;
	}

	public void setShowFaceDetection(boolean showFaceDetection) {		
		this.showFaceDetection = showFaceDetection;
	}

	public int getBlurEffect() {
		return blurEffect;
	}

	public void setBlurEffect(int blurEffect) {
		this.blurEffect = blurEffect;
	}

}