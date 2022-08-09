package com.casestudy.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.casestudy.instagrocer.commons.dto.OrderCreated;
import com.casestudy.inventory.dto.ProductDto;
import com.casestudy.inventory.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public interface ProductService {
	List<Product> getAllProducts(String currentUserUsername, String title, String category, String description,
			int page, int size);

	List<Product> getAllProducts(String currentUserUsername, String title, String category, String description);

	Product getProductById(String currentUserUsername, String productId);

	Product createProduct(String currentUserUsername, ProductDto dto);

	Product deleteProductById(String currentUserUsername, String productId);

	Product updateProductById(String currentUserUsername, String productId, ProductDto dto);

	Product changeQuantityOfProductBy(String currentUserUsername, String productId, int num);
	
	Product increaseQuantityOfProductBy(String currentUserUsername, String productId, int num);

	List<Product> getProductByOwner(int page, int size, String username);

	List<Product> getProductByOwner(String username);
	
	void reactToOrderCreatedEvent(OrderCreated orderCreated)  throws JsonProcessingException;
}
