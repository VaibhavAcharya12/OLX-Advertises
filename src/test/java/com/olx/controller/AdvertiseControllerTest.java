package com.olx.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olx.dto.Advertises;
import com.olx.service.AdvertiseService;

@WebMvcTest(AdvertiseController.class)
public class AdvertiseControllerTest {
	@MockBean
	private AdvertiseService advertiseService;
	
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	public void testcreateAdverties_Success()throws Exception {
		Advertises advertise = new Advertises();
		advertise.setTitle("Sofa for SAle");
		Advertises expextedAdvertise = new Advertises();
		expextedAdvertise.setTitle("Sofa for SAle");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "D78U");
		when(this.advertiseService.createAdverties("D78U", advertise))
		.thenReturn(expextedAdvertise);
		mockMvc.perform(post("/olx-advertise/advertise")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(advertise))
				.headers(headers)
			)
			 .andExpect(status().isCreated())
			 .andExpect(content().string(containsString("Sofa")))
			 .andReturn(); //call to rest API
			 
		  
	}
	
	@Test
	public void testUpdateAdverties_Success() throws JsonProcessingException, Exception {
		Advertises updateAd = new Advertises();
		updateAd.setTitle("Furniture sale");
		updateAd.setCategoryId(1);
		updateAd.setStatusId(1);
		Advertises expectedAd = new Advertises();
		expectedAd.setTitle("Furniture sale");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "D78U");
		when(this.advertiseService.updateAdverties("D78U", updateAd, 1)).thenReturn(expectedAd);
		mockMvc.perform(put("/olx-advertise/advertise/1")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(updateAd))
				.headers(headers)
			)
			 .andExpect(status().isOk())
			 .andExpect(content().string(containsString("Furniture")))
			 .andReturn();
		
	}
	
	@Test
	public void testGetAllAdvertisementsByUser() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "D78U");
		when(this.advertiseService.getAllAdvertisementsByUser("D78U")).thenReturn(new ArrayList<>());
		mockMvc.perform(get("/olx-advertise/user/advertise")
				.headers(headers)
			)
			 .andExpect(status().isOk())
			 .andReturn();
	}
	
	@Test
	public void testGetAdvertisementsByUser_Success() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "D78U");
		when(this.advertiseService.getAdvertisementsByUser("D78U",1)).thenReturn(new Advertises());
		mockMvc.perform(get("/olx-advertise/user/advertise/1")
				.headers(headers)
			)
			 .andExpect(status().isOk())
			 .andReturn();
	}
	
	@Test
	public void testDeleteAdvertisementsByUser() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "D78U");
		when(this.advertiseService.deleteAdvertisementsByUser("D78U",1)).thenReturn(true);
		mockMvc.perform(delete("/olx-advertise/user/advertise/1")
				.headers(headers)
			)
			 .andExpect(status().isOk())
			 .andReturn();
	}
	
	@Test
	public void testFilterAdvertisements() throws Exception {


		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "D78U");
		when(this.advertiseService.filterAdvertisementsBasedOnSearchText("abc")).thenReturn(new ArrayList<>());
		mockMvc.perform(get("/olx-advertise/advertise/search")
				.param("searchText", "abc")
				.headers(headers)
			)
			 .andExpect(status().isOk())
			 .andReturn();
	}
	
	@Test
	public void testFilterAdvertisementsByfilterCriteria() throws Exception {
		Advertises updateAd = new Advertises();
		updateAd.setTitle("Furniture sale");
		updateAd.setCategoryId(1);
		List<Advertises> list = Arrays.asList(updateAd);
		when(this.advertiseService.filterAdvertisements("abc", 0, "tom", null, null, null, null, null, 0, 0)).thenReturn(list);
		mockMvc.perform(get("/olx-advertise/advertise/search/filtercriteria")
				.param("searchText", "Furniture")
				.param("postedBy", "tom")
				.param("category","0")
			)
			 .andExpect(status().isOk())
			 .andReturn();
	}
	
	
	@Test
	public void getAdvertisementsDetails() throws Exception {
		Advertises updateAd = new Advertises();
		updateAd.setTitle("Furniture sale");
		updateAd.setCategoryId(1);
		updateAd.setStatusId(1);
		Advertises expectedAd = new Advertises();
		expectedAd.setTitle("Furniture sale");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "D78U");
		when(this.advertiseService.getAdvertisementsDetails("D78U",1)).thenReturn(updateAd);
		mockMvc.perform(get("/olx-advertise/advertise/1")
				.headers(headers)	
			)
			 .andExpect(status().isOk())
			 .andExpect(content().string(containsString("Furniture")))
			 .andReturn();
	}	
	

}
