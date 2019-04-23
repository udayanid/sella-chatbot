package com.util;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestTemplateHelper {
	
	private RestTemplate restTemplate;
	private ObjectMapper objectMapper;
	private static  RestTemplateHelper restTemplateHelper  = new RestTemplateHelper();
	public static RestTemplateHelper getInstance(){
		return restTemplateHelper;
	}
	public RestTemplate getRestTemplate(){
		//build the RestTemplate object of our own standard and return the reference. 
		this.restTemplate =  new RestTemplate();
		return restTemplate;
	}
	
	public ObjectMapper getObjectMapper(){
		//build the ObjectMapper object of our own standard and return the reference. 
		this.objectMapper =  new ObjectMapper();
		return objectMapper;
	}

}
