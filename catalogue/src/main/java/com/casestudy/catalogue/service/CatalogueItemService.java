package com.casestudy.catalogue.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.casestudy.catalogue.dto.CatalogueItemResponse;
import com.casestudy.catalogue.entity.CatalogueItem;
import com.casestudy.instagrocer.commons.dto.InventoryProduct;

@Service
public interface CatalogueItemService {
	List<CatalogueItemResponse> getAllItems(String title, String category);
	
	Page<CatalogueItemResponse> getAllItemsPaged(String title, String category, Integer page, Integer size);

	CatalogueItemResponse getItemByProductId(String productId);

	CatalogueItemResponse getById(Long id);

	CatalogueItem createCatalogueItem(InventoryProduct inventoryProduct);

	CatalogueItem updateCatalogueItem(InventoryProduct inventoryProduct);

	CatalogueItem deleteCatalogueItem(InventoryProduct inventoryProduct);
	
	void addRating(Long catalogueItemId, Integer rating, String currentUser);

	void removeRating(Long catalogueItemId, String currentUser);
}
