package com.casestudy.user.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.casestudy.user.utils.JWTAuthenticationEntryPoint;
import com.casestudy.user.utils.JWTRequestFilter;

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
				.antMatchers("/docs", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**", "/webjars/**", "/swagger-resources/**").permitAll()
				.antMatchers("/login", "/register").permitAll()
				.anyRequest().authenticated());
		
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}
