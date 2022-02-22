package com.olx.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.olx.dto.User;
import com.olx.exception.ServiceUnavailableException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class UserServiceDelegateImpl implements UserServiceDelegate {
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	@CircuitBreaker(name = "AUTH-CIRCUIT-BREAKER",fallbackMethod = "fallbackIsValidUser")
	public ResponseEntity<Boolean> isValidUser(String token) {
		HttpHeaders headrs = new HttpHeaders();
		headrs.set("Authorization", token);
		HttpEntity<Boolean> entity = new HttpEntity<Boolean>(headrs);
		ResponseEntity<Boolean> response = restTemplate.exchange("http://api-gateway/olx-auth/user/token/validate/", HttpMethod.GET, entity, Boolean.class);
		return response;
	}

	@Override
	@CircuitBreaker(name = "AUTH-CIRCUIT-BREAKER", fallbackMethod ="fallbackGetUSerInformation")
	public ResponseEntity<User> getUserInformation(String authToken) {
		HttpHeaders headrs = new HttpHeaders();
		headrs.set("Authorization", authToken);
		HttpEntity<User> entity = new HttpEntity<User>(headrs);
		ResponseEntity<User> response = restTemplate.exchange("http://api-gateway/olx-auth/user/", HttpMethod.GET, entity, User.class);
		return response;
	}
	
	public ResponseEntity<Boolean> fallbackIsValidUser(String token, Exception exception ) {
		throw new ServiceUnavailableException("User Service is unavailable, Please try after some time later");
		
	}
	
	public ResponseEntity<User> fallbackGetUSerInformation(String authToken, Exception ex) {
		throw new ServiceUnavailableException("User Service is unavailable, Please try after some time later");		
	}

}
