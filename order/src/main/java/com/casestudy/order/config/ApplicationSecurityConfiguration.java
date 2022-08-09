package com.casestudy.order.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.casestudy.order.components.JWTAuthenticationEntryPoint;
import com.casestudy.order.components.JWTRequestFilter;

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
		
		http.authorizeHttpRequests(autz -> autz
				.antMatchers("/docs", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
				.anyRequest().authenticated());
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
		
		http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}
