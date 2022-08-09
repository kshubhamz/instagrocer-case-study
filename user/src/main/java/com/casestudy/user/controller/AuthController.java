package com.casestudy.user.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.casestudy.user.dto.LoginCredential;
import com.casestudy.user.dto.UserDto;
import com.casestudy.user.entity.User;
import com.casestudy.user.service.UserService;
import com.casestudy.user.utils.JsonWebToken;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Authentication")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JsonWebToken jwt;
	
	@PostMapping("/login")
	@Operation(summary = "Logs In")
	public Map<String, Object> loginUser(@Valid @RequestBody LoginCredential credential) {
		return userService.loginUser(credential);
	}
	
	@PostMapping("/register")
	@ResponseStatus(code = HttpStatus.CREATED)
	@Operation(summary = "Register")
	public Map<String, Object> registerUser(@Valid @RequestBody UserDto dto) {
		User user = userService.createUser(dto);
		return Map.of("user", user,
				"token", jwt.generateToken(user.getName(), user.getUsername()));
	}
	
	@GetMapping("/users/{username}")
	@Operation(summary = "Get User Details by username", security = { @SecurityRequirement(name = "Authorization") })
	public User getUser(@PathVariable String username) {
		return userService.getUserByUsername(username);
	}
	
	@PostMapping("/users/{username}")
	@Operation(summary = "Update User Details by username", security = { @SecurityRequirement(name = "Authorization") })
	public User updateUser(@Valid @RequestBody UserDto dto, 
			@PathVariable String username,
			HttpServletRequest request) {
		String currentUser = (String) request.getAttribute("username");
		return userService.updateUser(currentUser, username, dto);
	}
	
	@PostMapping("/add-admin-role")
	@Operation(summary = "Add Admin Role", security = { @SecurityRequirement(name = "Authorization") })
	public User addAdminRole(@RequestParam String key, HttpServletRequest request) {
		String username = (String) request.getAttribute("username");
		return userService.addAdminRole(username, key);
	}

}
