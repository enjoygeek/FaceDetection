package com.cloud.dto;

import java.awt.geom.Rectangle2D;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FaceDetection {

	@JsonIgnore
	private Rectangle2D boundingBox;
	private Coordinate2D leftEye;
	private Coordinate2D rightEye;	
	
	public FaceDetection() {

	}

	public double getX() {
		return boundingBox.getX();
	}
	
	public double getY() {
		return boundingBox.getY();
	}
	
	public double getWidth(){
		return boundingBox.getWidth();
	}
	
	public double getHeight(){
		return boundingBox.getHeight();
	}

	public void setBoundingBox(Rectangle2D boundingBox) {
		this.boundingBox = boundingBox;
	}

	public Coordinate2D getLeftEye() {
		return leftEye;
	}

	public void setLeftEye(Coordinate2D leftEye) {
		this.leftEye = leftEye;
	}

	public Coordinate2D getRightEye() {
		return rightEye;
	}

	public void setRightEye(Coordinate2D rightEye) {
		this.rightEye = rightEye;
	}

}