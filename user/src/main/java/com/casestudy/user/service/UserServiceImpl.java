package com.casestudy.user.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.casestudy.user.dto.LoginCredential;
import com.casestudy.user.dto.UserDto;
import com.casestudy.user.entity.User;
import com.casestudy.user.enums.UserRole;
import com.casestudy.user.exceptions.AuthorizationException;
import com.casestudy.user.exceptions.BadRequestException;
import com.casestudy.user.exceptions.NotFoundException;
import com.casestudy.user.repository.UserRepository;
import com.casestudy.user.utils.JsonWebToken;

@Component
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JsonWebToken jwt;
	
	@Value("${salt_rounds}")
	private int saltRounds;
	
	@Value("${admin_role_secret}")
	private String adminRoleSecret;

	@Override
	public User createUser(UserDto dto) {
		User user = createUserObject(dto);
		return userRepository.save(user);
	}

	@Override
	public User getUserByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isEmpty()) {
			throw new NotFoundException("User doesn't exist with username " + username);
		}
		return user.get();
	}

	@Override
	public User updateUser(String currentUserUsername, String username, UserDto dto) {
		User user = getUserByUsername(username);
		
		// auth check
		if (!user.getUsername().equals(currentUserUsername)) {
			throw new AuthorizationException("Unauthorized to perform update.");
		}
		
		// password update
		if (dto.getOldPassword() != null) {
			if (user.getPassword() == null) {
				throw new BadRequestException("Old Password must not be empty to update password.");
			}
			
			// validate current password
			boolean isOldPasswordValid = BCrypt.checkpw(dto.getOldPassword(), user.getPassword());
			if (!isOldPasswordValid) {
				throw new BadRequestException("Old password doesn't match");
			}
			
			// set new password
			user.setPassword(hash(dto.getPassword()));
		}
		
		user.setName(dto.getName());
		// user.setUsername(dto.getUsername());
		
		return userRepository.save(user);
	}
	
	private User createUserObject(UserDto dto) {
		if (dto.getPassword() == null) {
			throw new BadRequestException("Password must be present to create a user.");
		}
		return new User(dto.getUsername(), dto.getName(), hash(dto.getPassword()));
	}
	
	private String hash(String str) {
		String salt = BCrypt.gensalt(saltRounds);
		return BCrypt.hashpw(str, salt);
	}

	@Override
	public Map<String, Object> loginUser(LoginCredential credentials) {
		String username = credentials.getUsername();
		String password = credentials.getPassword();
		User user = getUserByUsername(username);
		
		// validate password
		boolean isPasswordValid = BCrypt.checkpw(password, user.getPassword());
		if (!isPasswordValid) {
			throw new BadRequestException("Username and Password doesn't match.");
		}
		
		// generate token
		String token = jwt.generateToken(user.getName(), user.getUsername());
		
		return Map.of("name", user.getName(),
				"roles", user.getUserRoles(),
				"username", user.getUsername(),
				"token", token);
	}

	@Override
	public User addAdminRole(String username, String secret) {
		if (!secret.equals(adminRoleSecret)) {
			throw new BadRequestException("Invalid key provided to add Admin Role.");
		}
		User user = getUserByUsername(username);
		user.addUserRoles(UserRole.ROLE_ADMIN);
		return userRepository.save(user);
	}

}
