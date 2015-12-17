package com.cloud.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cloud.dto.ProcessResult;


public class Resumen {

	private int cantCarasReferencia;
	private int cantCarasDetectadas;
	private int puntosDetectados;
	private int porcentaje;
	private double promedioDistancia;	
	private double varianzaDistancia;
	private double desviacionDistancia;
	private double distanciaTotal;	
	
	
	HashMap<String, List<Distancias>> distancias;

	public Resumen() {
	}

	public int getCantCarasReferencia() {
		return cantCarasReferencia;
	}

	public void setCantCarasReferencia(int cantCarasReferencia) {
		this.cantCarasReferencia = cantCarasReferencia;
	}

	public int getCantCarasDetectadas() {
		return cantCarasDetectadas;
	}

	public void setCantCarasDetectadas(int cantCarasDetectadas) {
		this.cantCarasDetectadas = cantCarasDetectadas;
	}

	public void procesarDistancias(List<ProcessResult> resultados) {
		// para todas las imagenes del dataset
		// obtengo las distancias de esa imagen y la sumo
		double distanciaTotal = 0;
		double media = 0;
		double desviacion = 0.0;
		double varianza = 0.0;
		int elementos = 0;
		List<Distancias> dist;
		List<Double> valores = new ArrayList<>();
		for (ProcessResult pr : resultados) {
			dist = new ArrayList<Distancias>();
			dist = this.distancias.get(pr.getImage().getUri());
			if (dist != null) {
				for (Distancias d : dist) {	
					if (d.eye_left != -1.0){						
						valores.add(d.eye_left);
						distanciaTotal += Math.abs(d.eye_left);
						elementos++;
					}
					if (d.eye_right != -1.0){						
						valores.add(d.eye_right);
						distanciaTotal +=  Math.abs(d.eye_right);
						elementos++;
					}														
				}
			}
		}		
		if (valores.size() != 0) {
			media = distanciaTotal / valores.size();			
			for (int i = 0; i < valores.size(); i++) {
				double rango;
				rango = Math.pow(valores.get(i) - media, 2f);// hacemos la resta y elevamos al cuadrado				
				varianza = varianza + rango;
			}
			varianza = varianza / valores.size();
			desviacion = Math.sqrt(varianza);
		}
		setPuntosDetectados(valores.size());
		setDistanciaTotal(distanciaTotal);		
		setPromedioDistancia(media);
		setVarianzaDistancia(varianza);
		setDesviacionDistancia(desviacion);


	}

	public void setDistancias(HashMap<String, List<Distancias>> distancias) {
		this.distancias = distancias;

	}

	public int getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(int porcentaje) {
		this.porcentaje = porcentaje;
	}

	public double getPromedioDistancia() {
		return promedioDistancia;
	}

	public void setPromedioDistancia(double promedioDistancia) {
		this.promedioDistancia = promedioDistancia;
	}

	public double getVarianzaDistancia() {
		return varianzaDistancia;
	}

	public void setVarianzaDistancia(double varianzaDistancia) {
		this.varianzaDistancia = varianzaDistancia;
	}

	public double getDesviacionDistancia() {
		return desviacionDistancia;
	}

	public void setDesviacionDistancia(double desviacionDistancia) {
		this.desviacionDistancia = desviacionDistancia;
	}

	public double getDistanciaTotal() {
		return distanciaTotal;
	}

	public void setDistanciaTotal(double distanciaTotal) {
		this.distanciaTotal = distanciaTotal;
	}

	public int getPuntosDetectados() {
		return puntosDetectados;
	}

	public void setPuntosDetectados(int puntosDetectados) {
		this.puntosDetectados = puntosDetectados;
	}

}
