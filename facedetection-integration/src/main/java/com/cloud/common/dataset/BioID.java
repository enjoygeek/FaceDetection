package com.cloud.common.dataset;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cloud.dto.BioImage;
import com.cloud.dto.Image;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BioID {

	public static List<Image> loadImages(String path) {
		List<Image> images = new ArrayList<Image>();

		File dir = new File(path);
		File[] listFiles = dir.listFiles();
		if(listFiles == null || listFiles.length == 0) return images;
		
		List<File> directoryListing = Arrays.asList(listFiles);		
		
		directoryListing.stream().filter(f -> f.getName().endsWith(".eye"))
				.forEach(f -> {
					images.add(new BioImage(f.getAbsolutePath()));
					// Supongo que se llaman igual (.eye y .pgm)

					});

		return images;

	}
	
	public static void main(String[] args){
		List<Image> loadImages = BioID.loadImages("C:\\temp\\datasets\\bioid");
		
		try {
			System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(loadImages));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}

}
