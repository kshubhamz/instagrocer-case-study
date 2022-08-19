package com.casestudy.catalogue.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.casestudy.catalogue.dto.CatalogueItemResponse;
import com.casestudy.catalogue.service.CatalogueItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/items")
@Tag(name = "Catalogue Items")
public class CatalogueItemController {
	
	@Autowired
	private CatalogueItemService service;
	
	@GetMapping
	@Operation(summary = "Get All Catalogue Items")
	public Object fetchAllItems(
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String category
			) {
		if (size == null && page == null) {
			return service.getAllItems(title, category);
		} else {
			Page<CatalogueItemResponse> allItemsPaged = service.getAllItemsPaged(title, category, page, size);
			Map<String, Object> pagedRes = new LinkedHashMap<>();
			pagedRes.put("data", allItemsPaged.getContent());
			pagedRes.put("currentPage", allItemsPaged.getPageable().getPageNumber() + 1);
			pagedRes.put("totalPage", allItemsPaged.getTotalPages());
			pagedRes.put("currentSize", allItemsPaged.getNumberOfElements());
			pagedRes.put("totalSize", allItemsPaged.getTotalElements());
			return pagedRes;
		}
	}
	
	@GetMapping("/item-by-productId")
	@Operation(summary = "Get Catalogue Items By ProductID")
	public CatalogueItemResponse fetchByProductId(@RequestParam String productId) {
		return service.getItemByProductId(productId);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Get Catalogue Items By CatalogueID")
	public CatalogueItemResponse fetchByCatalogueId(@PathVariable Long id) {
		return service.getById(id);
	}
	
}
