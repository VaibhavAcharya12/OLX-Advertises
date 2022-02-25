package com.olx.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.olx.dto.Category;
import com.olx.dto.Status;
import com.olx.exception.CategoryNotFoundException;
import com.olx.exception.InvalidIdException;
import com.olx.exception.ServiceUnavailableException;
import com.olx.exception.StatusNotFoundException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
@Service
public class MasterDataServiceDelegateImpl implements MasterDataServiceDelegate {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Override
	@CircuitBreaker(name = "MASTERDATA-CIRCUIT-BREAKER", fallbackMethod = "fallbackGetCategoryById")
	public ResponseEntity<Category> getCategoryById(int categoryId) {
		return restTemplate.getForEntity("http://api-gateway/olx-materdata/advertise/category/"+ categoryId, Category.class);
	}

	@Override
	@CircuitBreaker(name = "MASTERDATA-CIRCUIT-BREAKER", fallbackMethod = "fallbackGetStatusById")
	public ResponseEntity<Status>  getStatusById(int statusId) {
		return restTemplate.getForEntity("http://api-gateway/olx-materdata/advertise/status/"+ statusId, Status.class);
	
	}

	@Override
	@CircuitBreaker(name = "MASTERDATA-CIRCUIT-BREAKER", fallbackMethod = "fallbackGetAllCategories")
	public ResponseEntity<Category[]> getAllCategories() {
		return restTemplate.getForEntity("http://api-gateway/olx-materdata/advertise/category", Category[].class);
	}

	@Override
	@CircuitBreaker(name = "MASTERDATA-CIRCUIT-BREAKER", fallbackMethod = "fallbackGetAllStatus")
	public ResponseEntity<Status[]> getAllStatus() {
		return restTemplate.getForEntity("http://api-gateway/olx-materdata/advertise/status", Status[].class);
	
	}
	
	public ResponseEntity<Category> fallbackGetCategoryById(int categoryId, Exception ex) {
		HttpClientErrorException exst = (HttpClientErrorException)ex;
		if(exst.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
			throw new CategoryNotFoundException("Status not found by ID "+ categoryId);
		}
		throw new ServiceUnavailableException("Masterdata Service is unavailable, Please try after some time later");
	}
	
	public ResponseEntity<Status>  fallbackGetStatusById(int statusId, Exception ex) {
		HttpClientErrorException exst = (HttpClientErrorException)ex;
		if(exst.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
			throw new StatusNotFoundException("Status not found by ID "+ statusId);
		}
		throw new ServiceUnavailableException("Masterdata Service is unavailable, Please try after some time later");
	}
	
	public ResponseEntity<Category[]> fallbackGetAllCategories(Exception ex) {
		throw new ServiceUnavailableException("Masterdata Service is unavailable, Please try after some time later");
	}
	
	public ResponseEntity<Status[]> fallbackGetAllStatus(Exception ex) {
		throw new ServiceUnavailableException("Masterdata Service is unavailable, Please try after some time later");
	}

}
