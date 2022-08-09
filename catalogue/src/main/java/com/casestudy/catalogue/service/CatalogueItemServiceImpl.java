package com.casestudy.catalogue.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.casestudy.catalogue.dao.CatalogueItemRepository;
import com.casestudy.catalogue.dao.RatingRepository;
import com.casestudy.catalogue.dto.CatalogueItemResponse;
import com.casestudy.catalogue.entity.CatalogueItem;
import com.casestudy.catalogue.entity.ItemRating;
import com.casestudy.instagrocer.commons.dto.InventoryProduct;
import com.casestudy.instagrocer.commons.exception.NotFoundException;

@Component
public class CatalogueItemServiceImpl implements CatalogueItemService {

	@Autowired
	private CatalogueItemRepository repository;
	
	@Autowired
	private RatingRepository ratingRepository;
	
	@Override
	public List<CatalogueItemResponse> getAllItems(String title, String category, Integer page, Integer size) {
		List<CatalogueItem> catalogueItems = null;
		if (page == null && size == null) {
			if (title == null && category != null) {
				catalogueItems = repository.findByCategoryOrderByTitleAscPriceAsc(category);
			} else if (title != null && category == null) {
				catalogueItems = repository.findByTitleOrderByPrice(title);
			} else {
				catalogueItems = repository.findAllByOrderByTitle();
			}
		} else {
			page = page == null ? 0 : page;
			size = size == null ? 10 : size;
			PageRequest pagedReq = PageRequest.of(page, size);
			
			Page<CatalogueItem> pagedRes = null;
			if (title == null && category != null) {
				pagedRes = repository.findByCategoryOrderByTitleAscPriceAsc(category, pagedReq);
			} else if (title != null && category == null) {
				pagedRes = repository.findByTitleOrderByPrice(title, pagedReq);
			} else {
				pagedRes = repository.findAllByOrderByTitle(pagedReq);
			}
			catalogueItems = pagedRes.getContent();
		}
		
		return catalogueItems.stream().map(item -> {
			CatalogueItemResponse itemResponse = CatalogueItemResponse.generateCatalogueItem(item);
			itemResponse.setRating(calculaterating(item));
			itemResponse.setNoOfRatings(item.getRatings().size());
			return itemResponse;
		}).collect(Collectors.toList());
	}

	@Override
	public CatalogueItemResponse getItemByProductId(String productId) {
		Optional<CatalogueItem> item = repository.findByProductId(productId);
		if (item.isEmpty()) {
			throw new NotFoundException(transformProductIdNotFound(productId));
		}
		
		CatalogueItem catalogueItem = item.get();
		CatalogueItemResponse itemResponse = CatalogueItemResponse.generateCatalogueItem(catalogueItem);
		itemResponse.setRating(calculaterating(catalogueItem));
		itemResponse.setNoOfRatings(catalogueItem.getRatings().size());
		return itemResponse;
	}

	@Override
	public CatalogueItemResponse getById(Long id) {
		Optional<CatalogueItem> item = repository.findById(id);
		if (item.isEmpty()) {
			throw new NotFoundException(transformCatalogueIdNotFoundMessage(id));
		}
		
		CatalogueItem catalogueItem = item.get();
		CatalogueItemResponse itemResponse = CatalogueItemResponse.generateCatalogueItem(catalogueItem);
		itemResponse.setRating(calculaterating(catalogueItem));
		itemResponse.setNoOfRatings(catalogueItem.getRatings().size());
		return itemResponse;
	}

	@Override
	public CatalogueItem createCatalogueItem(InventoryProduct inventoryProduct) {
		CatalogueItem catalogueItem = new CatalogueItem(
				inventoryProduct.getProductId(),
				inventoryProduct.getTitle(),
				inventoryProduct.getCategory(),
				inventoryProduct.getPrice(),
				inventoryProduct.getDescription(),
				inventoryProduct.getQuantity()
				);
		
		return repository.save(catalogueItem);
	}

	@Override
	public CatalogueItem updateCatalogueItem(InventoryProduct inventoryProduct) {
		String productId = inventoryProduct.getProductId();
		Optional<CatalogueItem> item = repository.findByProductId(productId);
		if (item.isEmpty()) {
			throw new NotFoundException(transformProductIdNotFound(productId));
		}
		
		CatalogueItem catalogueItem = item.get();
		
		// update property
		catalogueItem.setTitle(inventoryProduct.getTitle());
		catalogueItem.setCategory(inventoryProduct.getCategory());
		catalogueItem.setPrice(inventoryProduct.getPrice());
		catalogueItem.setDescription(inventoryProduct.getDescription());
		catalogueItem.setQuantity(inventoryProduct.getQuantity());
		
		return repository.save(catalogueItem);
	}

	@Override
	public CatalogueItem deleteCatalogueItem(InventoryProduct inventoryProduct) {
		String productId = inventoryProduct.getProductId();
		Optional<CatalogueItem> item = repository.findByProductId(productId);
		if (item.isEmpty()) {
			throw new NotFoundException(transformProductIdNotFound(productId));
		}
		
		CatalogueItem catalogueItem = item.get();
		
		// delete
		repository.deleteById(catalogueItem.getId());
		
		return catalogueItem;
	}
	
	private double calculaterating(CatalogueItem item) {
		return item.getRatings()
				.stream()
				.mapToInt(ItemRating::getRating)
				.average().orElse(0.0);
	}
	
	private String transformProductIdNotFound(String productId) {
		return "Catalogue Item with Product ID " + productId + " doesn't exist";
	}
	
	private String transformCatalogueIdNotFoundMessage(Long id) {
		return "Catalogue Item with ID " + id + " doesn't exist";
	}

	@Override
	public void addRating(Long id, Integer rating, String currentUser) {
		Optional<CatalogueItem> item = repository.findById(id);
		if (item.isEmpty()) {
			throw new NotFoundException(transformCatalogueIdNotFoundMessage(id));
		}
		
		CatalogueItem catalogueItem = item.get();
		
		// look for existing rating
		Optional<ItemRating> existingRatingIfAny = catalogueItem.getRatings()
				.stream()
				.filter(r -> 
					r.getUsername().equals(currentUser))
				.findFirst();
		
		if (existingRatingIfAny.isEmpty()) {
			// create rating
			ItemRating ratingObj = new ItemRating();
			ratingObj.setUsername(currentUser);
			ratingObj.setRating(rating);
			ratingObj.setCatalogueItem(catalogueItem);
			
			catalogueItem.addRating(ratingObj);
		} else {
			existingRatingIfAny.get().setRating(rating);
		}
		
		// save
		repository.save(catalogueItem);
	}

	@Override
	public void removeRating(Long id, String currentUser) {
		Optional<CatalogueItem> item = repository.findById(id);
		if (item.isEmpty()) {
			throw new NotFoundException(transformCatalogueIdNotFoundMessage(id));
		}
		
		CatalogueItem catalogueItem = item.get();
		
		Optional<ItemRating> itemRatingOpt = ratingRepository.findByUsernameAndCatalogueItem(currentUser, catalogueItem);
		
		if (itemRatingOpt.isEmpty()) {
			throw new NotFoundException("No any rating exist for this item.");
		}
		
		Long ratingId = itemRatingOpt.get().getId();
		
		// filter
		catalogueItem.setRatings(catalogueItem.getRatings()
				.stream()
				.filter(r -> !r.getUsername().equals(currentUser))
				.collect(Collectors.toList()));
		
		// save
		repository.save(catalogueItem);
		ratingRepository.deleteById(ratingId);
	}

}
