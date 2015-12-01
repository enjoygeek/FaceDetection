package com.cloud.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cloud.common.IService;
import com.cloud.common.dataset.BioID;
import com.cloud.dto.Image;
import com.cloud.dto.ProcessResult;
import com.cloud.local.OpenCVProcessor;

public class TestService {

	protected static Logger logger = Logger.getGlobal();
	
	private String dataset;
	private String face_cascade_url;
	private String eyes_cascade_url;
	
	private List<Image> images;
	private IService[] services;

	public TestService(String dataset, String faceCascadeClassifier, String eyeCascadeClassifier) {
		this.dataset = dataset;
		//TODO: Cargar desde el archivo de configuracion
		face_cascade_url = faceCascadeClassifier;
		eyes_cascade_url = eyeCascadeClassifier;
		 //new FacePlusPlus(FacePlusPlus.DEFAULT_ATTRIBUTES),
		services = new IService[] { new OpenCVProcessor(face_cascade_url,eyes_cascade_url) };
		images = BioID.loadImages(this.dataset);		
	}

	public List<ProcessResult> test() {
		List<ProcessResult> output = new ArrayList<ProcessResult>();
		for (IService service : services) {
			for (Image image : images) {
				try {
					output.add(service.run(image));
				} catch (Exception e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
		}
		return output;
	}

}
