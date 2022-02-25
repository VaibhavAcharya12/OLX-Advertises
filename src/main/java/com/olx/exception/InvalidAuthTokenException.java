package com.olx.exception;

public class InvalidAuthTokenException extends RuntimeException {
	

	public InvalidAuthTokenException(String message) {
		super(message);
	}

}
