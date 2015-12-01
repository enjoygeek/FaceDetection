package com.cloud.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cloud.common.IService;
import com.cloud.common.dataset.BioID;
import com.cloud.dto.Image;
import com.cloud.dto.ProcessResult;

public class TestService {

	protected static Logger logger = Logger.getGlobal();
	
	private String dataset;
	
	private List<Image> images;
	private List<IService> services;

	public TestService(String dataset) throws ExceptionInInitializerError{
		this.dataset = dataset;
		//TODO: Cargar desde el archivo de configuracion
		if (!new File(dataset).exists()){
			throw new ExceptionInInitializerError("Dataset not found");
		}
		
		 //new FacePlusPlus(FacePlusPlus.DEFAULT_ATTRIBUTES),
		//services = new IService[] { new OpenCVProcessor(face_cascade_url,eyes_cascade_url) };
		services = new ArrayList<IService>();
		images = BioID.loadImages(this.dataset);		
	}
	
	public void addService(IService service){
		this.services.add(service);
	}

	public List<ProcessResult> test() {
		List<ProcessResult> output = new ArrayList<ProcessResult>();
		for (IService service : services) {
			for (Image image : images) {
				System.out.println("Processing: "+image.getUri());
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
