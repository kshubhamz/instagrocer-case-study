package com.casestudy.user.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.casestudy.user.dto.LoginCredential;
import com.casestudy.user.dto.UserDto;
import com.casestudy.user.entity.User;

@Service
public interface UserService {
	User createUser(UserDto dto);

	User getUserByUsername(String username);

	User updateUser(String currentUserUsername, String username, UserDto dto);
	
	Map<String, Object> loginUser(LoginCredential credentials);
	
	User addAdminRole(String username, String secret);
}
