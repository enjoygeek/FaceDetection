package com.cloud.remote;

import java.util.List;
import java.util.Map;

import com.cloud.dto.Coordinate2D;
import com.cloud.dto.FaceDetection;
import com.cloud.dto.ProcessResult;

public class FacePlusPlusProcessResult extends ProcessResult{
	
	
	public FacePlusPlusProcessResult(String endpoint, Map<String, Object> output){
		super(endpoint, output);
		process(output);
	}

	private void process(Map<String,Object> processResult){
		//eyes = new ArrayList<Pair<Point2D,Point2D>>();
		//Get detected faces
		List<Object> faces = (List<Object>)processResult.get("face");
		//this.faceCount = faces.size();
		
		//For each face, recover eye positions
		for(Object face : faces){
			FaceDetection faceDetection = new FaceDetection();
			
			Map<String,Object> attributes = (Map<String, Object>) face;
			Map<String,Object> position = (Map<String, Object>) attributes.get("position");
			Map<String,Double> eye_left = (Map<String, Double>) position.get("eye_left");
			Map<String,Double> eye_right = (Map<String, Double>) position.get("eye_right");
			Coordinate2D ojoIzquierdo = new Coordinate2D(eye_left.get("x"),eye_left.get("y"));
			Coordinate2D ojoDerecho = new Coordinate2D(eye_right.get("x"),eye_right.get("y"));
			
			faceDetection.setLeftEye(ojoIzquierdo);
			faceDetection.setRightEye(ojoDerecho);
			
			this.faces.add(faceDetection);
		}
		
	}
}
