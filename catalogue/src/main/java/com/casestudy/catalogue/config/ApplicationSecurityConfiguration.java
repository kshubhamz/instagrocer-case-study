package com.casestudy.catalogue.config;

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

import com.casestudy.catalogue.component.JWTAuthenticationEntryPoint;
import com.casestudy.catalogue.component.JWTRequestFilter;

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
				.antMatchers(HttpMethod.GET).permitAll()
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
