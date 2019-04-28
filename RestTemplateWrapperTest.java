package com.learn.rest.client;

import java.util.HashMap;
import java.util.Map;

import com.learn.rest.Product;
import com.util.RestTemplateWrapper;

public class RestTemplateWrapperTest {

	public static void main(String[] args) {
		
	//	RestTemplateWrapper restTemplateWrapper = new RestTemplateWrapper(RestTemplateHelper.getInstance());
		
		//example for getForObject with two pathparams.
		/*String p = restTemplateWrapper.getForObject(String.class, "http://localhost:8081/RESTfulExample/json/product/getFullString/{name}/{name1}"
				,"anand","babu");
		System.out.println(p);*/
		
		/*//example for getForEntity without params
		Product p1 = restTemplateWrapper.getForEntity(Product.class, "http://localhost:8081/RESTfulExample/json/product/get");
		System.out.println(p1);
		*/
		//example for getForEntity without params as map
		Map<String, String> params = new HashMap<>(1);
		params.put("name", "Babuji");
		Product p3 = RestTemplateWrapper.INSTANCE.getForEntity(Product.class, "http://localhost:8081/RESTfulExample/json/product/getProdWithName/{name}",params);
		System.out.println(p3);
		
		//example for postForEntity without params as map
		Product p2 = RestTemplateWrapper.INSTANCE.postForEntity(Product.class, "http://localhost:8081/RESTfulExample/json/product/addProduct", p3);
		System.out.println(p2);
		
	/*	Map<String, String> params = new HashMap<>(1);
		params.put("name", "Babuji");
		Product p3 = restTemplateWrapper.getForEntity("http://localhost:8081/RESTfulExample/json/product/getProdWithName/{name}",params);
		System.out.println(p3);*/
		
		
		//example for postForEntity without params as map
		Product newProd = new Product();
		newProd.setName("Honda Bike");
		newProd.setQty(5);
				Product product = RestTemplateWrapper.INSTANCE.postForEntity(Product.class, "http://localhost:8081/RESTfulExample/json/product/addProduct", newProd);
				System.out.println(product);
	
				
	
	}
	

}
