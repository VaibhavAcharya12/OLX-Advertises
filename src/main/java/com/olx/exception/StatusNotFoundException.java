package com.olx.exception;

public class StatusNotFoundException extends RuntimeException {
	
 public StatusNotFoundException() {
		 
 }
 
 public StatusNotFoundException(String message) {
	 super(message);
 }
}
