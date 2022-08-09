package com.casestudy.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfiguration {
	@Bean
	  public OpenAPI springShopOpenAPI() {
	      return new OpenAPI()
	              .info(new Info().title("Orders Service")
	              .description("Orders Microservice Component for InstaGrocer [A Case Study]")
	              .version("v0.0.1")
	              .license(new License().name("Apache 2.0").url("http://springdoc.org")))
	              .externalDocs(new ExternalDocumentation()
	              .description(null)
	              .url(null))
	              .components(new Components()
	            		  .addSecuritySchemes("Authorization",
	                      new SecurityScheme()
	                      .type(SecurityScheme.Type.HTTP)
	                      .scheme("bearer").bearerFormat("JWT")));
	  }
}
