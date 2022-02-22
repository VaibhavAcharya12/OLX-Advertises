package com.olx.actuator;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import com.olx.dto.Advertises;
import com.olx.entity.AdvertisesEntity;
import com.olx.repository.AdvertisesRepository;

@Component
@Endpoint(id = "orders")
public class CustomOrderDetails {
	@Autowired
	private AdvertisesRepository advertisesRepository;
	@Autowired
	private ModelMapper modelMapper;

	@ReadOperation
	public List<Advertises> getAllUsers() {
		List<Advertises> adverties = new ArrayList<Advertises>();
		List<AdvertisesEntity> daoAdervertise = advertisesRepository.findAll();
		daoAdervertise.stream().forEach(advetise -> {
			adverties.add(modelMapper.map(advetise, Advertises.class));
		});
		return adverties;

	}

}
