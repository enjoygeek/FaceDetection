package com.cloud.remote;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cloud.common.IService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FacePlusPlus implements IService {
	
	protected static Logger logger = Logger.getGlobal();
	
	private final String serviceUrl = "https://faceplusplus-faceplusplus.p.mashape.com/detection/detect";
	private final String xMashapeKey = "1eIYzNA1P5msh6ThXy009FmSs5b3p1P2IoajsnAwdaN612TSBZ";
	private HashMap<String, String> parameters;
	public static String DEFAULT_ATTRIBUTES = "glass,pose,gender,age,race,smiling";

	public FacePlusPlus(String attributes) {
		parameters = new HashMap<String, String>();
		parameters.put("attribute", attributes);
	}

	public String run(String imageUri) {
		parameters.put("url", imageUri);
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("X-Mashape-Key", xMashapeKey);

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(serviceUrl);

		parameters.keySet().stream()
				.forEach(k -> builder.queryParam(k, parameters.get(k)));

		HttpEntity<String> entity = new HttpEntity<String>("parameters",
				headers);
				
		ResponseEntity<String> resultString = restTemplate.exchange(builder.build()
				.toUriString(), HttpMethod.GET, entity, String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> result = new HashMap<>();
		try{
			result = objectMapper.readValue(resultString.getBody(), HashMap.class);
		}catch(Exception e){
			logger.log(Level.SEVERE, e.getMessage());
			result.put("Result", "Response not valid");
		}
		
		return result.toString();

	}

}
