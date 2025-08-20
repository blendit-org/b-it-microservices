package com.blenditorg.auth_api.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blenditorg.auth_api.dtos.LoginUserDto;
import com.blenditorg.auth_api.dtos.RegisterUserDto;
import com.blenditorg.auth_api.entities.User;
import com.blenditorg.auth_api.exceptions.UsernameAlreadyExists;
import com.blenditorg.auth_api.repositories.UserRepository;

@Service
public class AuthenticationService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;
	
	public AuthenticationService(
			UserRepository userRepository,
			AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder
	) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User signup(RegisterUserDto input) {
		User user = new User();
		user.setFullName(input.getFullName());
		user.setEmail(input.getEmail());
		user.setPassword(passwordEncoder.encode(input.getPassword()));
		
		if (userRepository.existsById(input.getUserId())) {
			throw new UsernameAlreadyExists("This username " + input.getUserId() + " already exists");
		}
		
		user.setUserId(input.getUserId());
		
		return userRepository.save(user);
	}
	
	public User authenticate(LoginUserDto input) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
		System.out.println("[DEBUG]: AuthenticationService::authenticate(LoginUserDto input) called");
		return userRepository.findByEmail(input.getEmail()).orElseThrow();
	}
}
