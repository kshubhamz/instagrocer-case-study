package com.casestudy.order.feignclient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("catalogue-service")
public interface CatalogueServiceProxy {
	@GetMapping("/items/{id}")
	public Map<String, Object> fetchByCatalogueId(@PathVariable Long id);
}
