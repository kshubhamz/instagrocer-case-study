package com.casestudy.order.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.casestudy.order.dto.ModifyOrderDto;
import com.casestudy.order.dto.OrderDto;
import com.casestudy.order.entity.Order;
import com.casestudy.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	private static final String USERNAME_KEY = "username";
	
	@GetMapping("/{id}")
	@Operation(summary = "Get Order Details by OrderID", security = { @SecurityRequirement(name = "Authorization") })
	public Order fetchOrder(@PathVariable Long id, HttpServletRequest request) {
		String currentUser = (String) request.getAttribute(USERNAME_KEY);
		return orderService.getOrder(id, currentUser);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@Operation(summary = "Create an Order", security = { @SecurityRequirement(name = "Authorization") })
	public Order placeOrder(@Valid @RequestBody OrderDto[] dto, HttpServletRequest request) {
		String currentUser = (String) request.getAttribute(USERNAME_KEY);
		return orderService.placeOrder(dto, currentUser);
	}
	
	@PatchMapping("/{id}")
	@Operation(summary = "Cancel an Order by OrderID", security = { @SecurityRequirement(name = "Authorization") })
	public Order cancelOrder(@Valid @RequestBody ModifyOrderDto dto,
			@PathVariable Long id, HttpServletRequest request) {
		String currentUser = (String) request.getAttribute(USERNAME_KEY);
		return orderService.updateOrder(id, dto, currentUser);
	}
	
}
