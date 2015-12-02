package com.cloud.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cloud.dto.ProcessResult;
import com.vividsolutions.jts.algorithm.distance.DiscreteHausdorffDistance;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

public class Analisis {

	public static Resumen analizar(List<ProcessResult> resultados) {

		int cantCarasDetectadas = resultados.stream().mapToInt(pr -> pr.getFaceCount()).sum();
		int cantCarasReferencia = resultados.stream().mapToInt(pr -> pr.getFacesReference().size()).sum();
 
		// Se va a comparar ojo izq y derecho con los clasificados manualmente y
		// se quedará con el mas corto

		HashMap<String, List<Distancias>> distancias = new HashMap<>(); 
		List<Distancias> distanciasPr ;
		Double d1, d2,tmpD1,tmpD2;
		int xLDet,yLDet,xLRef,yLRef;
		int xRDet,yRDet,xRRef,yRRef;
		for (ProcessResult pr : resultados) {
			distanciasPr = new ArrayList<Distancias>();
			for (int i = 0; i < pr.getImage().getDetections().size(); i++) { // Itero por las caras detectadas de la imagen pr
				xLDet = pr.getImage().getDetections().get(i).getLeftEye().getX();
				yLDet = pr.getImage().getDetections().get(i).getLeftEye().getY();
				xRDet = pr.getImage().getDetections().get(i).getLeftEye().getX();
				yRDet = pr.getImage().getDetections().get(i).getLeftEye().getY();
				d1 = null;//Reinicio valores previos
				d2 = null;
				for (int j = 0; j < pr.getImage().getRefDetections().size(); j++) {
					// Ojo izquierdo
					xLRef = pr.getImage().getRefDetections().get(j).getLeftEye().getX();
					yLRef = pr.getImage().getRefDetections().get(j).getLeftEye().getY();
					tmpD1 = calcularDistancia(xLDet,yLDet,xLRef, yLRef);
					// Ojo derecho
					xRRef = pr.getImage().getRefDetections().get(j).getRightEye().getX();
					yRRef = pr.getImage().getRefDetections().get(j).getRightEye().getY();
					tmpD2 = calcularDistancia(xRDet,yRDet,xRRef,yRRef);
					if (d1 == null && d2 == null) {
						d1 = tmpD1;
						d2 = tmpD2;
					} else if ((tmpD1 + tmpD2) < (d1 + d2)) {
						d1 = tmpD1;
						d2 = tmpD2;
					}
				}
				distanciasPr.add(new Distancias(d1,d2));
			}
			distancias.put(pr.getImage().getUri(), distanciasPr);
		}
		Resumen resumen = new Resumen();		
		resumen.setCantCarasDetectadas(cantCarasDetectadas);
		resumen.setCantCarasReferencia(cantCarasReferencia);
		resumen.setDistancias(distancias);
		return resumen;
	}

	private static Double calcularDistancia(int xDet, int yDet, int xRef,int yRef) {		
		Coordinate cDet = new Coordinate(xDet,yDet);
		Coordinate cRef = new Coordinate(xRef,yRef);
		PrecisionModel p = new PrecisionModel();
		Point pointLeftDet = new Point(cDet, p, 1);
		Point pointLeftRef = new Point(cRef, p, 1);
		return DiscreteHausdorffDistance.distance(pointLeftDet, pointLeftRef);
	}

}
