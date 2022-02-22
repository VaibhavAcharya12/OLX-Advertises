package com.olx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olx.entity.AdvertisesEntity;

public interface AdvertisesRepository extends JpaRepository<AdvertisesEntity, Integer> {
	
	List<AdvertisesEntity> findByUserName(String userName);

}