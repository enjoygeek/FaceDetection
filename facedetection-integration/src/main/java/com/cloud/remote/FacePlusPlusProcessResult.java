package com.cloud.remote;

import java.util.List;
import java.util.Map;

import com.cloud.dto.Coordinate2D;
import com.cloud.dto.FaceDetection;
import com.cloud.dto.Image;
import com.cloud.dto.ProcessResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FacePlusPlusProcessResult extends ProcessResult {

	public FacePlusPlusProcessResult(String endpoint) {
		super(endpoint);
	}

	private Image parseImageData(Map<String, Object> processResult) {
		String url = String.valueOf(processResult.get("url"));
		Integer imgWidth = Integer.valueOf(String.valueOf(processResult
				.get("img_width")));
		Integer imgheight = Integer.valueOf(String.valueOf(processResult
				.get("img_height")));
		return new Image(url, imgWidth, imgheight, 0);
	}

	public FaceDetection parseFace(Map<String, Object> attributes) {
		FaceDetection faceDetection = new FaceDetection();
		Map<String, Object> position = (Map<String, Object>) attributes
				.get("position");
		Map<String, Double> eye_left = (Map<String, Double>) position
				.get("eye_left");
		Map<String, Double> eye_right = (Map<String, Double>) position
				.get("eye_right");

		Double[] eyeLeft = new Double[] {
				eye_left.get("x") / 100 * this.image.getWidth(),
				eye_left.get("y") / 100 * this.image.getHeight(), };
		Double[] eyeRight = new Double[] {
				eye_right.get("x") / 100 * this.image.getWidth(),
				eye_right.get("y") / 100 * this.image.getHeight(), };
		Coordinate2D ojoIzquierdo = new Coordinate2D(eyeLeft[0], eyeLeft[1]);
		Coordinate2D ojoDerecho = new Coordinate2D(eyeRight[0], eyeRight[1]);

		faceDetection.setLeftEye(ojoIzquierdo);
		faceDetection.setRightEye(ojoDerecho);
		return faceDetection;
	}

	public void process(Map<String, Object> processResult) {
		this.setServiceOutput(processResult.toString());

		this.setImage(parseImageData(processResult));
		// Get detected faces
		List<Object> faces = (List<Object>) processResult.get("face");

		// For each face, recover eye positions
		for (Object face : faces) {
			Map<String, Object> attributes = (Map<String, Object>) face;
			this.faces.add(parseFace(attributes));
		}

	}

	@Override
	public String toString() {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
