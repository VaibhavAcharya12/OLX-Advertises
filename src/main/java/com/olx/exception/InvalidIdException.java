package com.olx.exception;

public class InvalidIdException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public InvalidIdException(){}
	public InvalidIdException(String msg) {
		super(msg);
	}

}
