package com.cloud.dto;

import java.awt.geom.Rectangle2D;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FaceDetection {

	@JsonIgnore
	private Rectangle2D boundingBox;
	private Coordinate2D leftEye;
	private Coordinate2D rightEye;
	private Coordinate2D nose;
	private Coordinate2D mouth;
	
	public FaceDetection() {
		this.boundingBox = new Rectangle2D.Double();
		leftEye = null;
		rightEye = null;
		nose = null;
		mouth = null;
	}

	@JsonIgnore
	public double getX() {
		return boundingBox.getX();
	}
	
	@JsonIgnore
	public double getY() {
		return boundingBox.getY();
	}
	
	@JsonIgnore
	public double getWidth(){
		return boundingBox.getWidth();
	}
	
	@JsonIgnore
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

	public Coordinate2D getNose() {
		return nose;
	}

	public void setNose(Coordinate2D nose) {
		this.nose = nose;
	}

	public Coordinate2D getMouth() {
		return mouth;
	}

	public void setMouth(Coordinate2D mouth) {
		this.mouth = mouth;
	}

}
