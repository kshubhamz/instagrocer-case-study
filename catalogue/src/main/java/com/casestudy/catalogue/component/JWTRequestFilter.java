package com.casestudy.catalogue.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.Claim;
import com.casestudy.catalogue.config.AuthServiceProxy;
import com.casestudy.instagrocer.commons.dto.AuthenticatedUser;
import com.casestudy.instagrocer.commons.exception.NotFoundException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private JsonWebToken jwt;
	
	@Autowired
	private AuthServiceProxy authServiceProxy;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("authorization") == null
				? request.getHeader("Authorization")
				: request.getHeader("authorization");
		
		String username = null;
		
		if (requestTokenHeader != null) {
			Map<String, Claim> claims = jwt.getAllClaimsFromToken(requestTokenHeader.replace("Bearer", "").trim());
			
			username = jwt.getUsernameFromToken(claims);
		}
		
		if (username != null) {
			try {
				AuthenticatedUser user = authServiceProxy.getUserFromUsername(username, requestTokenHeader);
				
				request.setAttribute("username", user.getUsername());
				request.setAttribute("roles", user.getRoles());
				request.setAttribute("name", user.getName());
				
				// set authorities based on role of the request
				List<GrantedAuthority> authorities = new ArrayList<>();
				user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(username, null, authorities);
				usernamePasswordAuthenticationToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext()
					.setAuthentication(usernamePasswordAuthenticationToken);
			} catch (NotFoundException ex) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, 
						"UnAuthorized!! Details provided no longer exist.");
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
