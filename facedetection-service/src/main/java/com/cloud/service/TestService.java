package com.cloud.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cloud.common.IService;
import com.cloud.common.dataset.BioID;
import com.cloud.dto.Image;
import com.cloud.dto.ProcessResult;
import com.cloud.dto.BioImage;
import com.cloud.remote.FacePlusPlus;
import java.io.File;
import java.io.FilenameFilter;

public class TestService {
	
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
			for (String image : images) {
				output.add(service.run(image));
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
