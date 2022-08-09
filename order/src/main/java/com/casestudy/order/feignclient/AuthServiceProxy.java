package com.casestudy.order.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.casestudy.instagrocer.commons.dto.AuthenticatedUser;

@FeignClient("auth-service")
public interface AuthServiceProxy {
	@GetMapping("/users/{username}")
	public AuthenticatedUser getUserFromUsername(@PathVariable String username,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader);
}
