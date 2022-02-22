package com.olx.client;

import org.springframework.http.ResponseEntity;

import com.olx.dto.Category;
import com.olx.dto.Status;

public interface MasterDataServiceDelegate {
	
	public ResponseEntity<Category> getCategoryById(int categoryId);

	public ResponseEntity<Status> getStatusById(int statusId);

	public ResponseEntity<Category[]> getAllCategories();

	public ResponseEntity<Status[]> getAllStatus();

}
