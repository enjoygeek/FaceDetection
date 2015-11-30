package com.cloud.local;

import java.awt.geom.Rectangle2D;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
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

	public OpenCVProcessor(String face_cascade_url, String eyes_cascade_url) {
		////// Defino los clasificadores//////
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		setFaceClassifier( new CascadeClassifier(face_cascade_url));
		setEyesClassifier(new CascadeClassifier(eyes_cascade_url));
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

	void detectAndDisplay(Mat image) {
		CascadeClassifier faceDetector = new CascadeClassifier(OpenCVRunnerService.face_cascade_name);
		CascadeClassifier eyeDetector = new CascadeClassifier(OpenCVRunnerService.eyes_cascade_name);
		MatOfRect faces = new MatOfRect();
		Mat frame_gray = image;

		Imgproc.cvtColor(image, frame_gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(frame_gray, frame_gray);

		// -- Detect faces
		faceDetector.detectMultiScale(frame_gray, faces, 1.1, 2, 0, new Size(3, 3), new Size(3000, 3000));
		System.out.println("FACES: " + faces.size());
		for (Rect rect : faces.toArray()) {
			Point center = new Point(rect.x + rect.width * 0.5, rect.y + rect.height * 0.5);
			FaceDetection faceDetection = new FaceDetection();
			faceDetection.setBoundingBox(
					new Rectangle2D.Double(rect.width * 0.5, rect.height * 0.5, rect.width, rect.height));
			org.opencv.imgproc.Imgproc.ellipse(image, center, new Size(rect.width * 0.5, rect.height * 0.5), 0, 0, 360,
					new Scalar(255, 0, 255), 4, 8, 0);
			Mat faceROI = frame_gray;
			MatOfRect eyes = new MatOfRect();

			// -- In each face, detect eyes
			eyeDetector.detectMultiScale(faceROI, eyes, 1.1, 2, 0, new Size(30, 30), new Size(300, 300));
			System.out.println("EYES: " + eyes.size());
			for (Rect eye : eyes.toArray()) {
				System.out.println(eye.x + "-" + eye.y + "-" + eye.width + "-" + eye.height);
				Point center_eye = new Point(eye.x + eye.width * 0.5, eye.y + eye.height * 0.5);
				int radius = (int) Math.round((eye.width + eye.height) * 0.0005);

				org.opencv.imgproc.Imgproc.circle(image, center_eye, radius, new Scalar(255, 0, 0), 4, 8, 0);

			}
			Point eye_l, eye_r;
			int i = 0;
			// if (eyes.toArray()[i].x < eyes.toArray()[i+1].x){

			// };

		}
		// -- Show what you got
		Imshow im = new Imshow("Ejemplo");
		im.showImage(image);

	}

	public ProcessResult process(Image image) {
		// TODO devolver un processresult vacio
		if (image == null)
			return null;

		double fEscalar = 1.05; // Valores mas altos significan menos precisión.
								// 5% de incremento
		int minNeighbors = 3;// Valores mas altos significan menos precisión
								// pero mas fiabilidad
		int minSizeFace = (int) (image.getWidth() * 5 / 100);
		int maxSizeFace = (int) (image.getWidth() * 35 / 100);

		Coordinate2D leftEye, rightEye, nose, mouth;

		MatOfRect faceDetections = new MatOfRect();
		MatOfRect eyeDetections = new MatOfRect();
		Rect[] sortEyes;
		FaceDetection faceDetection;
		Rect rect_face;

		java.util.Date comienzo = new java.util.Date();
		///////////////////////////////
		// Comienzo detección de rostro
		///////////////////////////////
		getFaceClassifier().detectMultiScale(image.getMatImage(), faceDetections, fEscalar, minNeighbors, 0,
				new Size(minSizeFace, minSizeFace), new Size(maxSizeFace, maxSizeFace));

		List<FaceDetection> faces = new ArrayList<FaceDetection>();
		for (int r = 0; r < faceDetections.toArray().length; r++) {
			rect_face = faceDetections.toArray()[r];
			faceDetection = new FaceDetection();
			faceDetection.setBoundingBox(
					new Rectangle2D.Double(rect_face.x, rect_face.y, rect_face.width, rect_face.height));
			///////////////////////////////
			// Comienzo detección de ojos
			///////////////////////////////
			leftEye = rightEye = nose = mouth = null; // Reinicio los puntos del
														// rostro anterior
			Mat faceROI = image.getMatImage().submat(faceDetections.toArray()[r]);

			getEyesClassifier().detectMultiScale(faceROI, eyeDetections, fEscalar, minNeighbors, 0,
					new Size(minSizeFace / 50, minSizeFace / 50), new Size(maxSizeFace / 2, maxSizeFace / 2));

			if (eyeDetections.toArray().length > 1) {
				sortEyes = sortRect(eyeDetections.toArray());
				leftEye = new Coordinate2D(sortEyes[0].x, sortEyes[0].y);
				rightEye = new Coordinate2D(sortEyes[sortEyes.length - 1].x, sortEyes[sortEyes.length - 1].y);
				// El ultimo en el eje x es siempre el ojo derecho

				if (sortEyes.length == 3) {
					// Se detecto la nariz
					nose = new Coordinate2D(sortEyes[1].x, sortEyes[1].y);
				} else if (sortEyes.length == 4) {
					// Se detecto nariz y boca
					if (sortEyes[1].y < sortEyes[2].y) {
						nose = new Coordinate2D(sortEyes[1].x, sortEyes[1].y);
						mouth = new Coordinate2D(sortEyes[2].x, sortEyes[2].y);
					} else {
						mouth = new Coordinate2D(sortEyes[1].x, sortEyes[1].y);
						nose = new Coordinate2D(sortEyes[2].x, sortEyes[2].y);
					}
				}
			}
			/*
			 * else{ System.out.println("Se detectaron: "
			 * +eyeDetections.toArray().length+ " puntos"); }
			 * 
			 * for (int eyes = 0; eyes < eyeDetections.toArray().length;eyes++){
			 * //Dibujar los ojos int radius = 5; Point center_Eye = new
			 * Point(eyeDetections.toArray()[eyes].x,
			 * eyeDetections.toArray()[eyes].y);
			 * org.opencv.imgproc.Imgproc.circle(faceROI, center_Eye, radius,
			 * new Scalar(Math.min(5*eyes,255), Math.min(30*eyes,255),
			 * Math.min(75*eyes,255)), 5, 5, 0); } // Mostrar la cara analizada
			 * Imshow im = new Imshow( "Face number: "+r);
			 * im.showImage(faceROI);
			 */

			faceDetection.setLeftEye(leftEye);
			faceDetection.setRightEye(rightEye);
			faceDetection.setNose(nose);
			faceDetection.setMouth(mouth);
			faces.add(faceDetection);
		}

		long fin = new java.util.Date().getTime() - comienzo.getTime();
		Map<String, Object> resultado = new HashMap<String, Object>();
		resultado.put("elapsedTime", fin);
		resultado.put("faces", faces);
		resultado.put("path", image.getFile().getName());
		resultado.put("width", image.getWidth());
		resultado.put("height", image.getHeight());
		resultado.put("size", image.getFileSize());
		resultado.put("image", image);

		OpenCVProcessResult openCVProcessResult = new OpenCVProcessResult("OpenCV - " + face_cascade.toString());
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

	@SuppressWarnings("unused")
	private Mat setEffect(Mat imagenOrigen) {
		Mat imagenDestino = null;
		// ***Efectos sobre la imagen*******/
		/******* Blur *******/
		// Imgproc.GaussianBlur(imagenOrigen, imagenDestino, new
		// Size(45,45),0);
		// im.showImage(imagenDestino);

		/******* Gray *******/
		// Imgproc.cvtColor(image, image_gray, Imgproc.COLOR_BGR2GRAY);
		// //Gris
		// Imgproc.blur(imagenDestino, imagenDestino, new Size(9, 9));
		// im.showImage(imagenDestino);

		/******* Canny *******/
		// Imgproc.Canny(imagenOrigen, imagenDestino, 10, 100);
		// im.showImage(imagenDestino);

		/******* Threshold *******/
		// Imgproc.threshold(imagenOrigen, imagenDestino, 50, 150, 1);
		// im.showImage(imagenDestino);

		return imagenDestino;
	}

	@Override
	public ProcessResult run(Image image) {
		return process(image);
	}

}