package com.cloud.service;

import java.util.ArrayList;
import java.util.List;

import com.cloud.common.IService;
import com.cloud.dto.ProcessResult;
import com.cloud.remote.FacePlusPlus;

public class TestService {

	private String[] images = new String[] {
			"http://www.faceplusplus.com/wp-content/themes/faceplusplus/assets/img/demo/1.jpg",
			"http://www.weddingcostarica.com/images/pack1.jpg"
			};

	private IService[] services;

	public TestService() {
		services = new IService[] { new FacePlusPlus(FacePlusPlus.DEFAULT_ATTRIBUTES) };
	}

	public List<ProcessResult> test() {
		List<ProcessResult> output = new ArrayList<ProcessResult>();
		for (IService service : services) {
			for (String image : images) {
				output.add(service.run(image));
			}
		}
		return output;
	}
	
	public static void main (String[] args){
		TestService ts = new TestService();
		ts.test();
	}
}
