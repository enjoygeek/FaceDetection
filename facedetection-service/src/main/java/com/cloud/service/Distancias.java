package com.cloud.service;

public class Distancias {

	double eye_left;
	double eye_right;

	public Distancias(double eye_left, double eye_right) {
		this.eye_left = eye_left;
		this.eye_right = eye_right;
	}

	public double getEye_left() {
		return eye_left;
	}

	public void setEye_left(double eye_left) {
		this.eye_left = eye_left;
	}

	public double getEye_right() {
		return eye_right;
	}

	public void setEye_right(double eye_right) {
		this.eye_right = eye_right;
	}

}
