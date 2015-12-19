package com.cloud.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cloud.remote.FaceRect;

public class PerfRequestSyncInterceptor implements ClientHttpRequestInterceptor
{
	
	public double totalTime=0;
	
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		StopWatch stopwatch = new StopWatch();
		stopwatch.start();
	    ClientHttpResponse response = execution.execute(request, body);
	    stopwatch.stop();
	    
	    System.out.println(String.format(
	      "Sync %s request for '%s' took %dms and resulted in {3:d} - %s",
	      request.getMethod(),
	      request.getURI(),
	      stopwatch.getTotalTimeMillis(), 
	      response.getStatusCode()));
	    totalTime +=stopwatch.getTotalTimeMillis();
	    return response;  
	}
/*
	public static void main(String[] args){
		
		String xMashapeKey="1eIYzNA1P5msh6ThXy009FmSs5b3p1P2IoajsnAwdaN612TSBZ";
		String serviceUrl = "https://apicloud-facerect.p.mashape.com/process-url.json";
		
		RestTemplate restTemplate = new RestTemplate();
		PerfRequestSyncInterceptor perfRequestSyncInterceptor = new PerfRequestSyncInterceptor();
		restTemplate.setInterceptors(Arrays.asList(new ClientHttpRequestInterceptor[]{perfRequestSyncInterceptor}));
		
		

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("X-Mashape-Key", xMashapeKey);

		
		
		int count=0;
		double tiempoPorFuera=0;
		for(int i = 0; i<=100;i=i+10){
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("features", FaceRect.DEFAULT_ATTRIBUTES);
			parameters.put("url","http://cuys-srv.cloudapp.net/facedetection-cdn/data/bioid/data/BioID_0"+String.format("%03d", i)+".png");
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(serviceUrl);
			parameters.keySet().stream()
					.forEach(k -> builder.queryParam(k, parameters.get(k)));
	
			HttpEntity<String> entity = new HttpEntity<String>("parameters",
					headers);
			
			StopWatch sw = new StopWatch();
			sw.start();
			ResponseEntity<String> resultString = template.exchange(builder.build()
					.toUriString(), HttpMethod.GET, entity, String.class);
			sw.stop();
			tiempoPorFuera+= sw.getTotalTimeMillis();
			//End time measure
			parameters.clear();
			count++;
			
		}
		System.out.println("Total time: "+perfRequestSyncInterceptor.totalTime+"ms");
		System.out.println("Avg: "+perfRequestSyncInterceptor.totalTime/count+"ms");
		System.out.println("----------");
		System.out.println("Total time por fuera: "+tiempoPorFuera+"ms");
		System.out.println("Avg: "+tiempoPorFuera/count+"ms");
	}*/

	

}