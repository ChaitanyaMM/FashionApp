package com.fashionapp.serviceImpl;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fashionapp.service.FirebaseService;
import com.fashionapp.util.FireBaseResponse;
import com.fashionapp.util.HeaderRequestInterceptor;
@Service
public class FirebaseServiceImpl implements FirebaseService {

	private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
	private final Environment environment;

	@Autowired
	public FirebaseServiceImpl(Environment environment) {
		this.environment = environment;
	}

	 
	
	
	@Async
	public CompletableFuture<String> send(HttpEntity<String> entity) {
 
		RestTemplate restTemplate = new RestTemplate();
        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + environment.getProperty("firebaseServerKey")));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
		restTemplate.setInterceptors(interceptors);
 
		String firebaseResponse = restTemplate.postForObject(FCM_URL, entity, String.class);
 
		return CompletableFuture.completedFuture(firebaseResponse);
	}
	
	
	
	
	
	
	

}
