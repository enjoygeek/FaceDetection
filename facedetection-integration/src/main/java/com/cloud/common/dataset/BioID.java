package com.cloud.common.dataset;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.cloud.dto.BioImage;
import com.cloud.dto.Image;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BioID {

	public static final String extensionData = ".eye";
	public static final String extensionImage = ".pgm";

	public static List<Image> loadImages(String path) {
		List<Image> images = new ArrayList<Image>();

		File dir = new File(path);
		File[] listFiles = dir.listFiles();
		if (listFiles == null || listFiles.length == 0)
			return images;

		List<File> directoryListing = Arrays.asList(listFiles);

		// Supongo que se llaman igual (.eye y .pgm)
		directoryListing.stream()
			.filter(f -> f.getName().endsWith(extensionImage))
			.forEach(f -> {
				try{
				Image bI = new BioImage(f.getParent() + File.separator + f.getName());
				images.add(bI);
				}
				catch(Exception e){
					System.err.println(e.getMessage());
				}				
			});

		return images;

	}

	public static void main(String[] args) {
		List<Image> loadImages = BioID.loadImages("D:\\datasets\\BioID");

		try {
			System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(loadImages));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

}
