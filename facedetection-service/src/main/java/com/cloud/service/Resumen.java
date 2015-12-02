package com.cloud.service;

import java.util.HashMap;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

public class Resumen {

	private int cantCarasReferencia;
	private int cantCarasDetectadas;
	
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

	public HashMap<String, List<Distancias>> getDistancias() {
		return distancias;
	}

	public void setDistancias(HashMap<String, List<Distancias>> distancias) {
		this.distancias = distancias;
	}

}
