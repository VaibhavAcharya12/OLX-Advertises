package com.olx.client;

import org.springframework.http.ResponseEntity;

import com.olx.dto.User;

public interface UserServiceDelegate {
	
	ResponseEntity<Boolean> isValidUser(String token);

	ResponseEntity<User> getUserInformation(String authToken);

}
