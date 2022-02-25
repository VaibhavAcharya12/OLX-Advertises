package com.olx.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.olx.client.MasterDataServiceDelegate;
import com.olx.client.UserServiceDelegate;
import com.olx.dto.Advertises;
import com.olx.dto.Category;
import com.olx.dto.Status;
import com.olx.dto.User;
import com.olx.entity.AdvertisesEntity;
import com.olx.exception.InvalidAuthTokenException;
import com.olx.exception.InvalidDateRangeException;
import com.olx.exception.InvalidIdException;
import com.olx.exception.InvalidOnDateException;
import com.olx.exception.UnauthorizedUserException;
import com.olx.repository.AdvertisesRepository;
import com.olx.util.Constants;

@Service
public class AdvertiseServiceImpl implements AdvertiseService {

	@Autowired
	private AdvertisesRepository advertisesRepository;
	@Autowired
	private ModelMapper modelmapper;
	@Autowired
	private MasterDataServiceDelegate masterDataServiceDelegate;
	@Autowired
	private UserServiceDelegate userServiceDelegate;
	@Autowired
	private EntityManager entityManager;

	@Override
	public Advertises createAdverties(String authToken, Advertises advertises) {
		ResponseEntity<Boolean> userResponse = userServiceDelegate.isValidUser(authToken);
		if (userResponse.getBody()) {
			User user = userServiceDelegate.getUserInformation(authToken).getBody();
			Category category = masterDataServiceDelegate.getCategoryById(advertises.getCategoryId()).getBody();
			Status status = masterDataServiceDelegate.getStatusById(advertises.getStatusId()).getBody();
			advertises.setCreatedDate(LocalDateTime.now());
			advertises.setUsername(user.getUserName());
			AdvertisesEntity advertisesEntity = convertAdvertisesDTOtoAdvertisesEntity(advertises);
			advertisesEntity = advertisesRepository.save(advertisesEntity);
			advertises.setId(advertisesEntity.getId());
			advertises.setCategory(category.getCategory());
			advertises.setStatus(status.getStatus());
			return advertises;
		} else {
			throw new  InvalidAuthTokenException("Invalid token passed");
		}

	}

	@Override
	public Advertises updateAdverties(String authToken, Advertises advertises, int id) {
		boolean isValidUser = userServiceDelegate.isValidUser(authToken).getBody();
		if (isValidUser) {
			Optional<AdvertisesEntity> entity = advertisesRepository.findById(id);
			if (entity.isPresent()) {
				User user = userServiceDelegate.getUserInformation(authToken).getBody();
				if(entity.get().getUserName().equals(user.getUserName()) || user.getRoles().contains(Constants.ADMIN)) {
					Category category = masterDataServiceDelegate.getCategoryById(advertises.getCategoryId()).getBody();
					Status status = masterDataServiceDelegate.getStatusById(advertises.getStatusId()).getBody();
					advertises.setModifiedDate(LocalDateTime.now());
					advertises.setId(id);
					advertises.setUsername(user.getUserName());
					advertises.setCreatedDate(entity.get().getCreatedDate());
					AdvertisesEntity advertisesEntity = convertAdvertisesDTOtoAdvertisesEntity(advertises);
					advertisesRepository.save(advertisesEntity);
					advertises.setCategory(category.getCategory());
					advertises.setStatus(status.getStatus()); 
					return advertises;
				}else {
					throw new  UnauthorizedUserException("You don't have permission to perform this operation");
				}

			} else {
				throw new InvalidIdException("Invailid Id" + id);
			}
				
		} else {
			throw new InvalidAuthTokenException("Invalid token passed");
		}
			
	}

	@Override
	public List<Advertises> getAllAdvertisementsByUser(String authToken)  {
		boolean isValidUser = userServiceDelegate.isValidUser(authToken).getBody();
		if (isValidUser) {
			User user = userServiceDelegate.getUserInformation(authToken).getBody();
			List<AdvertisesEntity> advList = advertisesRepository.findByUserName(user.getUserName());
			List<Category> categories =Arrays.asList(masterDataServiceDelegate.getAllCategories().getBody());
			List<Status> statusList = Arrays.asList(masterDataServiceDelegate.getAllStatus().getBody());
			List<Advertises> advertieses = new ArrayList<Advertises>();
			advList.stream().forEach(advertise -> {
				Advertises advts = convertAdvertisesEntitytoAdvertisesDTO(advertise);
				Category category = filterCategoryById(categories, advts.getCategoryId());
				Status status = filterStatusById(statusList, advts.getStatusId());
				advts.setUsername(user.getUserName());
				advts.setCategory(category.getCategory());
				advts.setStatus(status.getStatus());
				advertieses.add(advts);
			});
			return advertieses;
		} else {
			throw new InvalidAuthTokenException("Invalid token passed");
		}
	}

	private Category filterCategoryById(List<Category> categories, int categoryId) {
		Optional<Category> categry = categories.stream()
				.filter(category -> category.getId() == categoryId)
				.findFirst();
		return categry.get();
	}

	private Status filterStatusById(List<Status> statusList, int statusId) {

		Optional<Status> status = statusList.stream()
				.filter(sts -> sts.getId() == statusId)
				.findFirst();
		return status.get();

	}

	@Override
	public Advertises getAdvertisementsByUser(String authToken, int advertiseId) {
		boolean isValidUser = userServiceDelegate.isValidUser(authToken).getBody();
		if (isValidUser) {
			Optional<AdvertisesEntity> advertisesEntity = advertisesRepository.findById(advertiseId);
			if (advertisesEntity.isPresent()) {
				User user = userServiceDelegate.getUserInformation(authToken).getBody();
				if(advertisesEntity.get().getUserName().equals(user.getUserName()) || user.getRoles().contains(Constants.ADMIN)) {
					Advertises advts = convertAdvertisesEntitytoAdvertisesDTO(advertisesEntity.get());
					Category category = masterDataServiceDelegate.getCategoryById(advts.getCategoryId()).getBody();
					Status status = masterDataServiceDelegate.getStatusById(advts.getStatusId()).getBody();
					advts.setUsername(user.getUserName());
					advts.setCategory(category.getCategory());
					advts.setStatus(status.getStatus());
					return advts;
				}
				else {
					throw new  UnauthorizedUserException("You don't have permission to perform this operation"); 
				}
			} else {
				throw new InvalidIdException("Invailid advertiseId " + advertiseId);
			}
				
		} else {
			throw new InvalidAuthTokenException("Invalid token passed");
		}
	}

	@Override
	public boolean deleteAdvertisementsByUser(String authToken, int advertiseId) {
		boolean isValidUser = userServiceDelegate.isValidUser(authToken).getBody();
		if (isValidUser) {
			Optional<AdvertisesEntity> advertisesEntity = advertisesRepository.findById(advertiseId);
			if (advertisesEntity.isPresent()) {
				User user = userServiceDelegate.getUserInformation(authToken).getBody();
				if(advertisesEntity.get().getUserName().equals(user.getUserName()) || user.getRoles().contains(Constants.ADMIN)) {
					advertisesRepository.delete(advertisesEntity.get());
					return true;
				}
				else {
					throw new  UnauthorizedUserException("You don't have permission to perform this operation");
				}
					 
			}
			else { 
				throw new InvalidIdException("Invailid advertiseId " + advertiseId);
			}
		}
		else {
			throw new InvalidAuthTokenException("Invalid token passed");
		}
			
	}

	@Override
	public List<Advertises> filterAdvertisementsBasedOnSearchText(String searchText) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AdvertisesEntity> criteriaQuery = criteriaBuilder.createQuery(AdvertisesEntity.class);
		Root<AdvertisesEntity> rootEntity = criteriaQuery.from(AdvertisesEntity.class);
		Predicate predicateSearchText = criteriaBuilder.and();
		if (searchText != null && !"".equals(searchText)) {
			Predicate predicateTitle = criteriaBuilder.like(rootEntity.get(Constants.ADMIN), "%" + searchText + "%");
			Predicate predicateDescription = criteriaBuilder.like(rootEntity.get(Constants.DESCRIPTION),
					"%" + searchText + "%");
			predicateSearchText = criteriaBuilder.or(predicateTitle, predicateDescription);
		}
		criteriaQuery.where(predicateSearchText);
		TypedQuery<AdvertisesEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		List<AdvertisesEntity> advertiseEntityList = typedQuery.getResultList(); // database call
		return convertEntityListIntoDTOList(advertiseEntityList);
	}

	@Override
	public Advertises getAdvertisementsDetails(String authToken, int advertiseId) {
		boolean isValidUser = userServiceDelegate.isValidUser(authToken).getBody();
		if (isValidUser) {
			Optional<AdvertisesEntity> advertisesEntity = advertisesRepository.findById(advertiseId);
			if (advertisesEntity.isPresent()) {
				return convertAdvertisesEntitytoAdvertisesDTO(advertisesEntity.get());
			}

		}
		  throw new InvalidAuthTokenException("Invalid Id passed");
	}

	private AdvertisesEntity convertAdvertisesDTOtoAdvertisesEntity(Advertises advertises) {
		return modelmapper.map(advertises, AdvertisesEntity.class);

	}

	private Advertises convertAdvertisesEntitytoAdvertisesDTO(AdvertisesEntity advertises) {
		return modelmapper.map(advertises, Advertises.class);

	}

	@Override
	public List<Advertises> filterAdvertisements(String searchText, int categoryId, String postedBy,
			String dateCondition, LocalDate onDate, LocalDate fromDate, LocalDate toDate, String sortBy,
			int startIndex, int records) {
		validatefileds(onDate,fromDate,toDate);
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AdvertisesEntity> criteriaQuery = criteriaBuilder.createQuery(AdvertisesEntity.class);
		Root<AdvertisesEntity> rootEntity = criteriaQuery.from(AdvertisesEntity.class);
		Predicate predicateSearchText = criteriaBuilder.and();
		Predicate predicateDateCondition = criteriaBuilder.and();
		Predicate predicateFinal = criteriaBuilder.and();
		Predicate predicateCategoryId = criteriaBuilder.and();
		Predicate predicatePostedBy = criteriaBuilder.and();
		if (searchText != null && !"".equals(searchText)) {
			Predicate predicateTitle = criteriaBuilder.like(rootEntity.get(Constants.TITLE), "%" + searchText + "%");
			Predicate predicateDescription = criteriaBuilder.like(rootEntity.get(Constants.DESCRIPTION),
					"%" + searchText + "%");
			predicateSearchText = criteriaBuilder.or(predicateTitle, predicateDescription);
		}
		if(postedBy!=null && !"".equals(postedBy)) {
			predicatePostedBy = criteriaBuilder.equal(rootEntity.get(Constants.POSTED_BY), postedBy);
		}
		if(categoryId!=0) {
			predicateCategoryId = criteriaBuilder.equal(rootEntity.get(Constants.CATEGORY_ID), categoryId);
		}
		
		if (dateCondition != null && !"".equalsIgnoreCase(dateCondition)) {
			if (dateCondition.equalsIgnoreCase(Constants.EQUALS)) {
				Predicate predicateEquals = criteriaBuilder.equal(rootEntity.get(Constants.CREATED_DATE), onDate.atStartOfDay());
				predicateDateCondition = criteriaBuilder.and(predicateEquals);
			}
			if(dateCondition.equalsIgnoreCase(Constants.GREATER_THAN)) {
				Predicate predicateGreterthan = criteriaBuilder.greaterThan(rootEntity.get(Constants.CREATED_DATE), fromDate.atStartOfDay());
				predicateDateCondition = criteriaBuilder.and(predicateGreterthan);
			}
			if(dateCondition.equalsIgnoreCase(Constants.LESS_THAN)) {
				Predicate predicateLessthan = criteriaBuilder.lessThan(rootEntity.get(Constants.CREATED_DATE), fromDate.atStartOfDay());
				predicateDateCondition = criteriaBuilder.and(predicateLessthan);
			}
			if(dateCondition.equalsIgnoreCase(Constants.BETWEEN)) {
				Predicate predicateBetween = criteriaBuilder.between(rootEntity.get(Constants.CREATED_DATE), fromDate.atStartOfDay(), toDate.atStartOfDay());
				predicateDateCondition = criteriaBuilder.and(predicateBetween);
			}
		}
		if(sortBy!=null &&!"".equals(sortBy)) {
		criteriaQuery.orderBy(criteriaBuilder.asc(rootEntity.get(sortBy)));	
		}
		predicateFinal = criteriaBuilder.and(predicateSearchText, predicateDateCondition, predicateCategoryId, predicatePostedBy);
		criteriaQuery.where(predicateFinal);
		TypedQuery<AdvertisesEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setFirstResult(startIndex);
		typedQuery.setMaxResults(records);
		List<AdvertisesEntity> advertiseEntityList = typedQuery.getResultList(); // database call
		return convertEntityListIntoDTOList(advertiseEntityList);
	}

	private void validatefileds(LocalDate onDate, LocalDate fromDate, LocalDate toDate) {
		boolean isFromDateAndToDatePresent = Optional.ofNullable(fromDate).isPresent() && Optional.ofNullable(fromDate).isPresent();
		boolean isOnDatePresent = Optional.ofNullable(onDate).isPresent();
		if(isOnDatePresent && onDate.isAfter(LocalDate.now())) {
			throw new InvalidOnDateException("Date should be past date or present date");
		}
		if(isFromDateAndToDatePresent && (fromDate.isAfter(toDate) || toDate.isBefore(fromDate))) {
			throw new InvalidDateRangeException();
		}
		
		
	}

	private List<Advertises> convertEntityListIntoDTOList(List<AdvertisesEntity> advertiseEntityList) {
		List<Advertises> list = advertiseEntityList.stream()
				.map(entity -> convertAdvertisesEntitytoAdvertisesDTO(entity)).collect(Collectors.toList());
		return list;
	}
}
