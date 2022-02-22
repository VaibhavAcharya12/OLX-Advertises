package com.olx.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.olx.dto.Category;
import com.olx.dto.Status;
import com.olx.exception.ServiceUnavailableException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
@Service
public class MasterDataServiceDelegateImpl implements MasterDataServiceDelegate {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Override
	@CircuitBreaker(name = "MASTERDATA-CIRCUIT-BREAKER", fallbackMethod = "fallbackGetCategoryById")
	public ResponseEntity<Category> getCategoryById(int categoryId) {
		ResponseEntity<Category> category = restTemplate.getForEntity("http://api-gateway/olx-materdata/advertise/category/"+ categoryId, Category.class);
		return category;
	}

	@Override
	@CircuitBreaker(name = "MASTERDATA-CIRCUIT-BREAKER", fallbackMethod = "fallbackGetStatusById")
	public ResponseEntity<Status>  getStatusById(int statusId) {
		ResponseEntity<Status> status = restTemplate.getForEntity("http://api-gateway/olx-materdata/advertise/status/"+ statusId, Status.class);
		return status;
	
	}

	@Override
	@CircuitBreaker(name = "MASTERDATA-CIRCUIT-BREAKER", fallbackMethod = "fallbackGetAllCategories")
	public ResponseEntity<Category[]> getAllCategories() {
		ResponseEntity<Category[]> category = restTemplate.getForEntity("http://api-gateway/olx-materdata/advertise/category", Category[].class);
		//List<Category> categories = Arrays.asList(category.getBody());
		return  category;
	}

	@Override
	@CircuitBreaker(name = "MASTERDATA-CIRCUIT-BREAKER", fallbackMethod = "fallbackGetAllStatus")
	public ResponseEntity<Status[]> getAllStatus() {
		ResponseEntity<Status[]> status = restTemplate.getForEntity("http://api-gateway/olx-materdata/advertise/status", Status[].class);
		//List<Status> statusList = Arrays.asList(status.getBody());
		return status ;
	
	}
	
	public ResponseEntity<Category> fallbackGetCategoryById(int categoryId, Exception ex) {
		throw new ServiceUnavailableException("Masterdata Service is unavailable, Please try after some time later");
	}
	
	public ResponseEntity<Status>  fallbackGetStatusById(int statusId, Exception ex) {
		
		throw new ServiceUnavailableException("Masterdata Service is unavailable, Please try after some time later");
	}
	
	public ResponseEntity<Category[]> fallbackGetAllCategories(Exception ex) {
		throw new ServiceUnavailableException("Masterdata Service is unavailable, Please try after some time later");
	}
	
	public ResponseEntity<Status[]> fallbackGetAllStatus(Exception ex) {
		throw new ServiceUnavailableException("Masterdata Service is unavailable, Please try after some time later");
	}

}
