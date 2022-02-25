package com.olx.exception;

public class InvalidOnDateException extends RuntimeException{
	
	public InvalidOnDateException() {
		
	}
	
	public InvalidOnDateException(String message) {
		super(message);
	} 

}
