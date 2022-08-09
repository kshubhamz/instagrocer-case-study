package com.casestudy.catalogue.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casestudy.catalogue.dto.RatingReq;
import com.casestudy.catalogue.service.CatalogueItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/ratings")
@Tag(name = "Catalogue Items Rating")
public class CatalogueItemRatingController {
	
	@Autowired
	private CatalogueItemService service;
	
	@PatchMapping("/{catalogueItemId}")
	@Operation(summary = "Give Rating to a Catalogue Item", security = { @SecurityRequirement(name = "Authorization") })
	public Map<String, String> giveRating(@Valid @RequestBody RatingReq ratingReq, 
			@PathVariable Long catalogueItemId,
			HttpServletRequest request) {
		String username = (String) request.getAttribute("username");
		service.addRating(catalogueItemId, ratingReq.getRating(), username);
		return Map.of("message", "Rated " + ratingReq.getRating() + " successfully.");
	}
	
	@DeleteMapping("/{catalogueItemId}")
	@Operation(summary = "Remove Rating from a Catalogue Item", security = { @SecurityRequirement(name = "Authorization") })
	public Map<String, String> removeRating(@PathVariable Long catalogueItemId,HttpServletRequest request) {
		String username = (String) request.getAttribute("username");
		service.removeRating(catalogueItemId, username);
		return Map.of("message", "Rating removed successfully.");
	}

}
