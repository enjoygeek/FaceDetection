package com.cloud.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cloud.common.IService;
import com.cloud.common.dataset.BioID;
import com.cloud.dto.Image;
import com.cloud.dto.ProcessResult;
import com.cloud.remote.FacePlusPlus;

public class TestService {
	
	protected static Logger logger = Logger.getGlobal();
	
	private List<Image> images;	
	private IService[] services;

	public TestService() {
		services = new IService[] { new FacePlusPlus(
				FacePlusPlus.DEFAULT_ATTRIBUTES) };
		images = BioID.loadImages("D:\\datasets\\BioID");
	}

	
	
	public List<ProcessResult> test() {
		List<ProcessResult> output = new ArrayList<ProcessResult>();
		for (IService service : services) {
			for (Image image : images) {
				try{
					output.add(service.run(image));
				}
				catch(Exception e){
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
		}
		return output;
	}

	public static void main(String[] args) {
		TestService ts = new TestService();
		for(ProcessResult pr : ts.test()){
			System.out.println(pr);
		}
	}
}
