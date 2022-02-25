package com.olx.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.olx.util.Constants;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value= {InvalidIdException.class, StatusNotFoundException.class, CategoryNotFoundException.class,InvalidDateRangeException.class})
	public ResponseEntity<Object> handleBadRequest(RuntimeException exception, WebRequest request){
		return handleExceptionInternal(exception, "\"error\": \""+ exception.toString(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
		
	}
	
	@ExceptionHandler(value = {InvalidAuthTokenException.class})
	public ResponseEntity<Object> handleAuthorization(RuntimeException exception, WebRequest request){
		return handleExceptionInternal(exception, Constants.ERROR_STRING + exception.toString(), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
	}
	
	@ExceptionHandler(value = {UnauthorizedUserException.class,})
	public ResponseEntity<Object> handleAuthentication(RuntimeException exception, WebRequest request){
		return handleExceptionInternal(exception, Constants.ERROR_STRING + exception.toString(), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}
	
	@ExceptionHandler(value = {ServiceUnavailableException.class, Exception.class})
	public ResponseEntity<Object> handleGenricException(RuntimeException exception, WebRequest request){
		return handleExceptionInternal(exception, Constants.ERROR_STRING + exception.toString(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	
	
}
