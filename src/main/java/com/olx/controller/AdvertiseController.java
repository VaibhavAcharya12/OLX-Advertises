package com.olx.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olx.dto.Advertises;
import com.olx.service.AdvertiseService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/olx-advertise")
@CrossOrigin(origins = "*")
public class AdvertiseController {

	@Autowired
	private AdvertiseService advertiseService;

	@PostMapping(value = "/advertise", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ApiOperation(value = "Create New Advertise", notes = "Creates a new advertise for loged in User")
	public ResponseEntity<Advertises> createAdverties(@RequestHeader("Authorization") String authToken,
			@RequestBody Advertises request)  {
		return new ResponseEntity<>(advertiseService.createAdverties(authToken, request), HttpStatus.CREATED);
	}

	@PutMapping(value = "/advertise/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ApiOperation(value = "Update Advertise", notes = "Updates existing advertise by user")
	public ResponseEntity<Advertises> updateAdverties(@RequestHeader("Authorization") String authToken,
			@RequestBody Advertises request, @PathVariable("id") int id)  {
		return new ResponseEntity<>(advertiseService.updateAdverties(authToken, request, id), HttpStatus.OK);

	}
	
	@GetMapping(value = "/user/advertise", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ApiOperation(value = "Get Advertisement List", notes = "Gives list of advertise posted by the user")
	public ResponseEntity<List<Advertises>> getAllAdvertisementsByUser(@RequestHeader("Authorization") String authToken)
			 {
		return new ResponseEntity<>(advertiseService.getAllAdvertisementsByUser(authToken), HttpStatus.OK);

	}

	@GetMapping(value = "/user/advertise/{advertiseId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ApiOperation(value = "Get Advertise by advertiseId ", notes = "Returns Advertises Posted by the User")
	public ResponseEntity<Advertises> getAdvertisementsByUser(@RequestHeader("Authorization") String authToken,
			@PathVariable("advertiseId") int advertiseId)  {
		return new ResponseEntity<>(advertiseService.getAdvertisementsByUser(authToken, advertiseId), HttpStatus.OK);

	}

	@DeleteMapping(value = "/user/advertise/{advertiseId}")
	@ApiOperation(value = "Delete advertise by Id", notes = "Deletes Advertise By ID")
	public ResponseEntity<Boolean> deleteAdvertisementsByUser(@RequestHeader("Authorization") String authToken,
			@PathVariable("advertiseId") int advertiseId) {
		return new ResponseEntity<>(advertiseService.deleteAdvertisementsByUser(authToken, advertiseId), HttpStatus.OK);

	}

	@GetMapping(value = "/advertise/search/filtercriteria", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ApiOperation(value = "filters Advertisement", notes = "filters Advertisement based on diffrent criterias")
	public ResponseEntity<List<Advertises>> filterAdvertisementsByFilterCriteria(@RequestParam(name="searchText", required = false)String searchText,
			@RequestParam(name = "category", defaultValue="0")Integer categoryId, @RequestParam(name="postedBy", required=false)String postedBy,
			@RequestParam(name="dateCondition", required=false)String dateCondition,
			@RequestParam(name="onDate", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate onDate,
			@RequestParam(name="fromDate", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam(name="toDate", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
			@RequestParam(name="sortedBy", required=false)String sortedBy, @RequestParam(name = "startIndex", defaultValue="0")int startIndex,
			@RequestParam(name="records", defaultValue = "10")int records) {
		
		return new ResponseEntity<>(advertiseService.filterAdvertisements(searchText, categoryId, postedBy, dateCondition, onDate, fromDate,
				toDate, sortedBy, startIndex, records), HttpStatus.OK);

	}

	@GetMapping(value = "/advertise/search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ApiOperation(value = "filter Advertisement by Any String", notes = "filters Advertisement based on diffrent any String")
	public ResponseEntity<List<Advertises>> filterAdvertisementsBasedOnSearchText(@RequestParam("searchText") String searchText) {
		return new ResponseEntity<>(advertiseService.filterAdvertisementsBasedOnSearchText(searchText),HttpStatus.OK);

	}

	@GetMapping(value = "/advertise/{advertiseId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ApiOperation(value = "Advetise Details", notes = "Returns Advetise Details")
	public ResponseEntity<Advertises> getAdvertisementsDetails(@RequestHeader("Authorization") String authToken,
			@PathVariable("advertiseId") int advertiseId) {
		return new ResponseEntity<>(advertiseService.getAdvertisementsDetails(authToken, advertiseId), HttpStatus.OK);

	}

	public AdvertiseService getAdvertiseService() {
		return advertiseService;
	}

	public void setAdvertiseService(AdvertiseService advertiseService) {
		this.advertiseService = advertiseService;
	}

}
