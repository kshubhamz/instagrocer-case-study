package com.casestudy.inventory.config;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.casestudy.inventory.components.JWTAuthenticationEntryPoint;
import com.casestudy.inventory.components.JWTRequestFilter;

@Configuration
public class ApplicationSecurityConfiguration {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, 
			JWTAuthenticationEntryPoint authenticationEntryPoint,
			JWTRequestFilter requestFilter) throws Exception {
		http.csrf().disable();
		
		http.cors().configurationSource(req -> {
			CorsConfiguration cors = new CorsConfiguration();
			cors.addAllowedOrigin("*");
			cors.setAllowedMethods(List.of("*"));
			cors.setAllowedHeaders(List.of("*"));
			return cors;
		});
		
		final String ADMIN_ROLE = "ADMIN";
		final String PRODUCT_ANT_MATCHER = "/products/**";
		
		http.authorizeHttpRequests(autz -> autz
				.antMatchers("/docs", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
				.antMatchers(HttpMethod.GET, "/products/my-products").hasRole(ADMIN_ROLE)
				.antMatchers(HttpMethod.GET).permitAll()
				.antMatchers(HttpMethod.POST, "/products").hasRole(ADMIN_ROLE)
				.antMatchers(HttpMethod.PATCH, PRODUCT_ANT_MATCHER).hasAnyRole(ADMIN_ROLE)
				.antMatchers(HttpMethod.PUT, PRODUCT_ANT_MATCHER).hasRole(ADMIN_ROLE)
				.antMatchers(HttpMethod.DELETE, PRODUCT_ANT_MATCHER).hasRole(ADMIN_ROLE)
				.anyRequest().authenticated()
		);
		
		http.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
			.and()
			.exceptionHandling()
				.accessDeniedHandler((request, response, accessDeniedException) -> 
					response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden!!"));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}
