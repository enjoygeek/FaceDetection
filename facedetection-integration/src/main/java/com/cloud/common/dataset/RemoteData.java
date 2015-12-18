package com.cloud.common.dataset;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cloud.common.NativeLibraries;
import com.cloud.dto.BioImage;
import com.cloud.dto.Image;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RemoteData {
	
	protected static Logger logger = Logger.getGlobal();
	
	public static List<String> loadDataset(String endpoint){
		List<String> datasets = new ArrayList<String>();
		
		//Service consumption
		RestTemplate restTemplate = new RestTemplate();
		logger.log(Level.INFO, "Calling "+endpoint);
		ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(endpoint, String[].class);
		String[] output =responseEntity.getBody();
		HttpStatus statusCode = responseEntity.getStatusCode();
		if(statusCode.is2xxSuccessful()){
			datasets = Arrays.asList(output);
		}else{
			logger.log(Level.SEVERE, statusCode.toString());
		}
		
		return datasets;
	}
	
	public static List<Image> loadImages(String listEndpoint, String imageEndpoint) {
		List<Image> images = new ArrayList<Image>();
		List<String> files = loadDataset(listEndpoint);
		for(String file : files){
			try{
				images.add(new Image(imageEndpoint+file, -1, -1, true));
			}catch(Exception ex){
				logger.log(Level.SEVERE, ex.getMessage());
			}
		}
		
		return images;

	}

	public static void main(String[] args) {
		NativeLibraries.loadLibraries();
		String dataset = "http://cuys-srv.cloudapp.net/facedetection-cdn/api/Assets/bioid";
		String imageService = "http://cuys-srv.cloudapp.net/facedetection-cdn/api/Image/";
		Image.BASE_PATH_TO_DOWNLOAD="/Users/Alejandro/Documents/Facultad/Cloud/facedetection-folder/upload";
		List<Image> loadImages = RemoteData.loadImages(dataset,imageService);
		for(Image i : loadImages){
			System.out.println(i.getWidth()+"-"+i.getHeight());
		}
		System.out.println("Image count: "+ loadImages.size());
	}

}
