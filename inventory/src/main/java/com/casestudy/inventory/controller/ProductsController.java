package com.casestudy.inventory.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.casestudy.inventory.dto.ProductDto;
import com.casestudy.inventory.entity.Product;
import com.casestudy.inventory.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/products")
@Tag(name = "Inventory")
public class ProductsController {
	@Autowired
	private ProductService productService;
	
	static final String USERNAME_KEY = "username";
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@Operation(summary = "Create an Inventory Item", security = { @SecurityRequirement(name = "Authorization") })
	public Product createProduct(@Valid @RequestBody ProductDto dto, HttpServletRequest request) {
		String currentUserUsername = (String) request.getAttribute(USERNAME_KEY);
		return productService.createProduct(currentUserUsername, dto);
	}
	
	@GetMapping
	@Operation(summary = "Get All Orders", 
	description = "Response is sorted by title. InventoryItems can be searched by title, category & description.")
	public List<Product> searchProduct(
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) Integer page,
			HttpServletRequest request
			) {
		title = title == null ? "" : title;
		category = category == null ? "" : category;
		description = description == null ? "" : description;
		String username = (String) request.getAttribute(USERNAME_KEY);
		
		if (size == null && page == null) {
			return productService.getAllProducts(username, title, category, description);
		} else {
			size = size == null ? 10 : size;
			page = page == null ? 0 : page;
			return productService.getAllProducts(username, title, category, description, page, size);
		}
	}
	
	@GetMapping("/my-products")
	@Operation(summary = "Get self-created InventoryItems", security = { @SecurityRequirement(name = "Authorization") })
	public List<Product> getSelfProduct(
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) Integer page,
			HttpServletRequest request) {
		String username = (String) request.getAttribute(USERNAME_KEY);
		
		if (size == null && page == null) {
			return productService.getProductByOwner(username);
		} else {
			size = size == null ? 10 : size;
			page = page == null ? 0 : page;
			return productService.getProductByOwner(page, size, username);
		}
	}
	
	@GetMapping("/{productId}")
	@Operation(summary = "Get InventoryItems by ID")
	public Product getProductById(@PathVariable String productId, HttpServletRequest request) {
		String username = (String) request.getAttribute(USERNAME_KEY);
		return productService.getProductById(username, productId);
	}
	
	@DeleteMapping("/{productId}")
	@Operation(summary = "Delete InventoryItems by ID", security = { @SecurityRequirement(name = "Authorization") })
	public Map<String, Object> deleteProductById(@PathVariable String productId, HttpServletRequest request) {
		String username = (String) request.getAttribute(USERNAME_KEY);
		Product product = productService.deleteProductById(username, productId);
		return Map.of("message", "Product deleted successfully.",
				"product", product);
	}
	
	@PutMapping("/{productId}")
	@Operation(summary = "Update InventoryItems by ID", security = { @SecurityRequirement(name = "Authorization") })
	public Product updateProduct(@PathVariable String productId,
			@Valid @RequestBody ProductDto dto,
			HttpServletRequest request
			) {
		String username = (String) request.getAttribute(USERNAME_KEY);
		return productService.updateProductById(username, productId, dto);
	}
	
	@PatchMapping("/{productId}/increase-stock")
	@Operation(summary = "Increase InventoryItem quantity", security = { @SecurityRequirement(name = "Authorization") })
	public Product increaseQuantityOfProduct(@PathVariable String productId,
			@RequestParam("by") Integer increaseBy,
			HttpServletRequest request) {
		String username = (String) request.getAttribute(USERNAME_KEY);
		return productService.increaseQuantityOfProductBy(username, productId, increaseBy);
	}
	
}
