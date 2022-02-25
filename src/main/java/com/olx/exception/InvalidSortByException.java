package com.olx.exception;

public class InvalidSortByException extends RuntimeException{
	public InvalidSortByException() {
		
	}
	
	public InvalidSortByException(String message) {
		super(message);
	} 

}
