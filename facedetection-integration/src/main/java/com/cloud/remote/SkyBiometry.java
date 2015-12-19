package com.cloud.remote;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cloud.common.IService;
import com.cloud.common.PerfRequestSyncInterceptor;
import com.cloud.dto.Image;
import com.cloud.dto.ProcessResult;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SkyBiometry implements IService {
	
	protected static Logger logger = Logger.getGlobal();
	
	private String serviceUrl;
	private String xMashapeKey;
	private HashMap<String, String> parameters;
	MultiValueMap<String,String> mvn;
	public static String DEFAULT_ATTRIBUTES = "none";//all, none or a comma separated list of supported attributes 
	public static String DEFAULT_DETECTOR = "default";//Normal (default) – fast face and attribute detection, Aggressive – more accurate and slower face and attribute detection

	public SkyBiometry(String serviceUrl, String xMashapeKey,String api_key, String api_secret, String attributes, String detector) {
		parameters = new HashMap<String, String>();
		mvn = new LinkedMultiValueMap<String,String>();
		this.serviceUrl = serviceUrl;
		this.xMashapeKey = xMashapeKey;
		parameters.put("api_key", api_key);
		parameters.put("api_secret", api_secret);
		mvn.add("attributes", attributes);
		mvn.add("detector", detector);
		
		
	}

	public ProcessResult run(Image image) {
		ProcessResult resultado = new SkyBiometryProcessResult(this.serviceUrl);
		//TODO Completar tamaÃ±o y peso de la imagen en resultado
		resultado.setImage(image);	
		mvn.remove("urls");
		//mvn.replace("urls", Arrays.asList(new String[]{image.getRemoteUri()}));
		mvn.add("urls", image.getRemoteUri());
		RestTemplate restTemplate = new RestTemplate();
		PerfRequestSyncInterceptor perfRequestSyncInterceptor = new PerfRequestSyncInterceptor();
		restTemplate.setInterceptors(Arrays.asList(new ClientHttpRequestInterceptor[]{perfRequestSyncInterceptor}));

		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("X-Mashape-Key", xMashapeKey);

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(serviceUrl);

		parameters.keySet().stream()
				.forEach(k -> builder.queryParam(k, parameters.get(k)));
		
		
		HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<MultiValueMap<String,String>>(mvn,
				headers);
				


		//Time measure
		String uri = builder.build().toUriString();
		resultado.setStartTime(System.nanoTime());
		//Actual processing		
		ResponseEntity<String> resultString = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		//End time measure
		resultado.setEndTime(System.nanoTime());		
		resultado.setInternalTime(perfRequestSyncInterceptor.totalTime);
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> result = new HashMap<>();
		try{
			String body = resultString.getBody();
			result = objectMapper.readValue(body, HashMap.class);
		}catch(Exception e){
			logger.log(Level.SEVERE, e.getMessage());
			result.put("Result", "Response not valid");
		}
		
		try {
			resultado.process(result);
		} catch (Exception e) {
			result.put("Result", e.getMessage());
		}
		return resultado;

	}

}
