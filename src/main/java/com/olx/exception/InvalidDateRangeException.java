package com.olx.exception;

public class InvalidDateRangeException  extends RuntimeException{
	public InvalidDateRangeException() {
		
	}
	
	public InvalidDateRangeException(String message) {
		super(message);
	}

}
