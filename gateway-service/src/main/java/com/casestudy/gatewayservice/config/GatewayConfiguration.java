package com.casestudy.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {
	
	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder routeBuilder) {
		return routeBuilder.routes()
				.route("auth-service", route -> route.path("/auth/**")
						.filters(path -> path.rewritePath("/auth/(?<segment>.*)", "/${segment}"))
						.uri("lb://auth-service"))
				.route("inventory-service", route -> route.path("/inventory/**")
						.filters(path -> path.rewritePath("/inventory/(?<segment>.*)", "/${segment}"))
						.uri("lb://inventory-service"))
				.route("catalogue-service", route -> route.path("/catalogue/**")
						.filters(path -> path.rewritePath("/catalogue/(?<segment>.*)", "/${segment}"))
						.uri("lb://catalogue-service"))
				.route("order-service", route -> route.path("/orders/**")
						.filters(path -> path.rewritePath("/orders/v3/api-docs", "/v3/api-docs"))
						.uri("lb://order-service"))
				.route(route -> route.path("/v3/api-docs/**")
						.filters(path -> path.rewritePath("/v3/api-docs/(?<path>.*)", "/${path}/v3/api-docs"))
						.uri("lb://gateway-service"))
				.build();
	}
}
