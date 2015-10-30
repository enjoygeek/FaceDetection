package com.cloud.dto;

import java.awt.geom.Rectangle2D;

public class FaceDetection {

	private Rectangle2D boundingBox;
	private Coordinate2D leftEye;
	private Coordinate2D rightEye;

	public FaceDetection() {

	}

	public Rectangle2D getBoundingBox() {
		return boundingBox;
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
