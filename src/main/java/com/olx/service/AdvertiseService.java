package com.olx.service;

import java.time.LocalDate;
import java.util.List;

import com.olx.dto.Advertises;

public interface AdvertiseService {

	Advertises createAdverties(String authToken, Advertises request);

	Advertises updateAdverties(String authToken, Advertises request, int id);

	List<Advertises> getAllAdvertisementsByUser(String authToken);

	Advertises getAdvertisementsByUser(String authToken, int advertiseId);

	boolean deleteAdvertisementsByUser(String authToken, int advertiseId);

	List<Advertises> filterAdvertisements(String searchText, int categoryId, String postedBy,
			String dateCondition, LocalDate onDate, LocalDate fromDate, LocalDate toDate, String sortBy,
			int startIndex, int records);

	List<Advertises> filterAdvertisementsBasedOnSearchText(String searchText);

	Advertises getAdvertisementsDetails(String authToken, int advertiseId);

}
