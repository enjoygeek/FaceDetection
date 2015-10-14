package com.cloud.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cloud.common.IService;
import com.cloud.remote.FacePlusPlus;


public class TestService {
	
	private String[] images = new String[]{
			"http://www.faceplusplus.com/wp-content/themes/faceplusplus/assets/img/demo/1.jpg"
			};
	
	private IService[] services;
	
	public TestService(){
		services = new IService[]{
				new FacePlusPlus(FacePlusPlus.DEFAULT_ATTRIBUTES)
				};
	}
	
	public String test(){
		List<String> output = new ArrayList<String>();
		for(IService service : services){
			for(String image : images){
				String run = service.run(image);
				output.add(run);
			}			
		}
		return Arrays.toString(output.toArray());
	}
}
