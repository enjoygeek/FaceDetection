package com.cloud.dto;

public class Coordinate2D {

	private double x;
	private double y;
	
	public Coordinate2D(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	public Coordinate2D(){}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
}
