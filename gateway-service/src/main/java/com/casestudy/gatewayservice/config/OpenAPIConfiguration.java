package com.casestudy.gatewayservice.config;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfiguration {
	@Bean
	  public OpenAPI springShopOpenAPI() {
	      return new OpenAPI()
	              .info(new Info().title("InstaGrocer")
	              .description("InstaGrocer(A case study) Backend Service")
	              .version("v0.0.1")
	              .license(new License().name("Apache 2.0").url("http://springdoc.org")))
	              .externalDocs(new ExternalDocumentation()
	              .description(null)
	              .url(null));
	  }
	
	@Bean
	public List<GroupedOpenApi> openAPIs(RouteLocator locator, SwaggerUiConfigParameters configParameters) {
		List<GroupedOpenApi> groups = new ArrayList<>();
		List<Route> definitions = locator.getRoutes().collectList().block();
		
		if (definitions != null) {
			definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
				String serviceName = routeDefinition.getId().replace("-service", "");
				configParameters.addGroup(serviceName);
				GroupedOpenApi.builder().pathsToMatch("/" + serviceName + "/**").group(serviceName).build();
			});
		}
	
		return groups;
	}
}
