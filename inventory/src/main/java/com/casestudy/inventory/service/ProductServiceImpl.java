package com.casestudy.inventory.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.casestudy.instagrocer.commons.constant.InstaGrocerRouting;
import com.casestudy.instagrocer.commons.constant.InstaGrocerTopicExchange;
import com.casestudy.instagrocer.commons.dto.InventoryProduct;
import com.casestudy.instagrocer.commons.dto.OrderCreated;
import com.casestudy.instagrocer.commons.dto.ProductOrdered;
import com.casestudy.instagrocer.commons.dto.UnAvailableOrderedProduct;
import com.casestudy.instagrocer.commons.exception.AuthorizationException;
import com.casestudy.instagrocer.commons.exception.InvalidDataException;
import com.casestudy.instagrocer.commons.exception.NotFoundException;
import com.casestudy.instagrocer.commons.exception.RabbitMQMessagePublishException;
import com.casestudy.instagrocer.commons.exception.UnSupportedOperationException;
import com.casestudy.inventory.dto.ProductDto;
import com.casestudy.inventory.entity.Product;
import com.casestudy.inventory.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private RabbitMessagingTemplate messagingTemplate;

	@Override
	public List<Product> getAllProducts(String currentUserUsername, String title, String category, String description, int page, int size) {
		PageRequest pagable = PageRequest.of(page, size);
		Page<Product> pagedProducts;
		if (currentUserUsername != null)
			pagedProducts = productRepository.findAllProductsSortByTitle(title, category, description, pagable);
		else
			pagedProducts = productRepository.findAllProductsSortByTitleWithoutOwner(title, category, description, pagable);
		return pagedProducts.getContent();
	}

	@Override
	public Product getProductById(String currentUserUsername, String productId) {
		validateAndParseObjectId(productId);
		Optional<Product> product = productRepository.findById(productId);
		if (product.isEmpty()) {
			throw new NotFoundException(transformProductNotFoundMessage(productId));
		}
		
		if (currentUserUsername == null) product.get().setOwnerUsername(null);
		
		return product.get();
	}

	@Override
	public Product createProduct(String currentUserUsername, ProductDto dto) {
		Product product = createProductObject(dto);
		product.setOwnerUsername(currentUserUsername);
		final Product newProduct = productRepository.save(product);
		
		// publish inventory-product-created event
		final InventoryProduct publishableInventoryItem = InventoryProduct.createMessagableInventoryProduct(newProduct);
		
		try {
			messagingTemplate.convertAndSend(InstaGrocerTopicExchange.INVENTORY, 
					InstaGrocerRouting.INVENTORY_ITEM_CREATE_ROUTE, 
					publishableInventoryItem.toJsonString());
		} catch (MessagingException | JsonProcessingException e) {
			productRepository.delete(newProduct);
			throw new RabbitMQMessagePublishException(e.getLocalizedMessage());
		}
		
		return newProduct;
	}

	@Override
	public Product deleteProductById(String currentUserUsername, String productId) {
		Product product = getProductById(currentUserUsername, productId);
		
		// auth-check
		if (!product.getOwnerUsername().equals(currentUserUsername)) {
			throw new AuthorizationException("Unauthorized to update this product.");
		}
		
		productRepository.deleteById(productId);
		
		// publish inventory-product-deleted event
		final InventoryProduct publishableInventoryItem = InventoryProduct.createMessagableInventoryProduct(product);
		try {
			messagingTemplate.convertAndSend(InstaGrocerTopicExchange.INVENTORY,
					InstaGrocerRouting.INVENTORY_ITEM_DELETE_ROUTE,
					publishableInventoryItem.toJsonString());
		} catch (MessagingException | JsonProcessingException e) {
			productRepository.save(product);
			throw new RabbitMQMessagePublishException(e.getLocalizedMessage());
		}
		
		return product;
	}

	@Override
	public Product updateProductById(String currentUserUsername, String productId, ProductDto dto) {
		validateAndParseObjectId(productId);
		Optional<Product> productOpt = productRepository.findById(productId);
		if (productOpt.isEmpty()) {
			throw new NotFoundException(transformProductNotFoundMessage(productId));
		}
		
		final Product product = productOpt.get();
		
		// copy property in case of any message-publish exception
		Product tempProduct = new Product();
		BeanUtils.copyProperties(product, tempProduct);
		
		// auth-check
		if (!product.getOwnerUsername().equals(currentUserUsername)) {
			throw new AuthorizationException("Unauthorized to update this product.");
		}
		
		// update properties
		product.setTitle(dto.getTitle());
		product.setCategory(dto.getType());
		product.setDescription(dto.getDescription());
		product.setPrice(dto.getPrice());
		product.setQuantity(dto.getQuantity());
		
		final Product savedProduct = productRepository.save(product);
		
		// publish inventory-item-updated event
		publishInventoryItemUpdatedEvent(tempProduct, savedProduct);
		
		return savedProduct;
	}
	
	@Override
	public Product changeQuantityOfProductBy(String currentUserUsername, String productId, int num) {
		Optional<Product> productOpt = productRepository.findById(productId);
		if (productOpt.isEmpty()) {
			throw new NotFoundException(transformProductNotFoundMessage(productId));
		}
		
		final Product product = productOpt.get();
		
		// copy property in case of any message-publish exception
		Product tempProduct = new Product();
		BeanUtils.copyProperties(product, tempProduct);
		
		int newQuantity = product.getQuantity() + num;
		if (newQuantity < 0) {
			throw new UnSupportedOperationException("Cannot decrease existing quantity(" + product.getQuantity()
					+ ") by " + num + " for product " + productId + ".");
		}
		
		product.setQuantity(newQuantity);
		final Product savedProduct = productRepository.save(product);
		
		// publish inventory-item-updated event
		publishInventoryItemUpdatedEvent(tempProduct, savedProduct);
		
		if (currentUserUsername == null) savedProduct.setOwnerUsername(null);
		
		return savedProduct;
	}
	
	@Override
	public Product increaseQuantityOfProductBy(String currentUserUsername, String productId, int num) {
		Optional<Product> productOpt = productRepository.findById(productId);
		if (productOpt.isEmpty()) {
			throw new NotFoundException(transformProductNotFoundMessage(productId));
		}
		
		final Product product = productOpt.get();
		
		if (!product.getOwnerUsername().equals(currentUserUsername)) {
			throw new AuthorizationException("Unauthorized to update this product.");
		}
		
		return changeQuantityOfProductBy(currentUserUsername, productId, num);
	}
	
	@Override
	public List<Product> getAllProducts(String currentUserUsername, String title, String category, String description) {
		if (currentUserUsername != null)
			return productRepository.findAllProductsSortByTitle(title, category, description);
		else
			return productRepository.findAllProductsSortByTitleWithoutOwner(title, category, description);
	}

	@Override
	public List<Product> getProductByOwner(int page, int size, String username) {
		PageRequest pagable = PageRequest.of(page, size);
		Page<Product> pagedProducts = productRepository.findByOwnerUsernameOrderByTitle(username, pagable);
		return pagedProducts.getContent();
	}

	@Override
	public List<Product> getProductByOwner(String username) {
		return productRepository.findByOwnerUsernameOrderByTitle(username);
	}

	private Product createProductObject(ProductDto dto) {
		return new Product(dto.getTitle(), 
				dto.getType(), 
				dto.getDescription(), 
				dto.getPrice(),
				dto.getQuantity());
	}
	
	private void validateAndParseObjectId(String objectIdString) {
		if (!ObjectId.isValid(objectIdString)) {
			throw new InvalidDataException(objectIdString + " is not a valid ID.");
		}
	}

	private void publishInventoryItemUpdatedEvent(Product tempProduct, final Product savedProduct) {
		final InventoryProduct publishableInventoryItem = InventoryProduct.createMessagableInventoryProduct(savedProduct);
		try {
			messagingTemplate.convertAndSend(InstaGrocerTopicExchange.INVENTORY,
					InstaGrocerRouting.INVENTORY_ITEM_UPDATE_ROUTE,
					publishableInventoryItem.toJsonString());
		} catch (MessagingException | JsonProcessingException e) {
			productRepository.save(tempProduct);
			throw new RabbitMQMessagePublishException(e.getLocalizedMessage());
		}
	}
	
	private String transformProductNotFoundMessage(String productId) {
		return "Product with ID " + productId + " doesn't exist.";
	}

	@Override
	public void reactToOrderCreatedEvent(OrderCreated order)  throws JsonProcessingException {
		// product availability check
		boolean isAnyProductNotAvailable = order.getProducts().stream().anyMatch(product -> {
			String productId = product.getProductId();
			Product inventoryProduct = getProductById(null, productId);
			return inventoryProduct.getQuantity() < product.getQuantity();
		});

		// publish product-unavailability event
		if (isAnyProductNotAvailable) {
			messagingTemplate.convertAndSend(InstaGrocerTopicExchange.ORDER, 
					InstaGrocerRouting.ORDER_FAILURE, 
					order.toJsonString());
			return;
		}
		

		// update quantity in inventory
		for (ProductOrdered product : order.getProducts()) {
			String productId = product.getProductId();
			Long quantity = product.getQuantity();

			try {
				changeQuantityOfProductBy(null, productId, -quantity.intValue());
			} catch (UnSupportedOperationException | NotFoundException ex) {
				// publish some-product-unavailability event
				UnAvailableOrderedProduct unAvailableOrderedProduct = new UnAvailableOrderedProduct();
				unAvailableOrderedProduct.setOrderId(order.getOrderId());
				unAvailableOrderedProduct.setProductId(productId);
				unAvailableOrderedProduct.setCatalogueItemId(product.getCatalogueItemId());
				messagingTemplate.convertAndSend(InstaGrocerTopicExchange.ORDER,
						InstaGrocerRouting.PRODUCT_ORDER_SOME_UNAVAILABLE_ROUTE,
						unAvailableOrderedProduct.toJsonString());
			}

		}

		// publish order-success event
		messagingTemplate.convertAndSend(InstaGrocerTopicExchange.ORDER, 
				InstaGrocerRouting.ORDER_SUCCESS, 
				order.toJsonString());
	}

}
