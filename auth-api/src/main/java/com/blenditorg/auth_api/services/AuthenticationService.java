package com.blenditorg.auth_api.services;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blenditorg.auth_api.dtos.LoginUserDto;
import com.blenditorg.auth_api.dtos.RegisterUserDto;
import com.blenditorg.auth_api.entities.User;
import com.blenditorg.auth_api.exceptions.UserNotVerifiedError;
import com.blenditorg.auth_api.exceptions.UsernameAlreadyExists;
import com.blenditorg.auth_api.repositories.UserRepository;

@Service
public class AuthenticationService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;	
	
	public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, MailService mailService) {
		super();
		System.out.println("[debug] AuthenticationService() constructor called");
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
	}
	
	public User signup(RegisterUserDto input) {
		
		User user = new User();
		user.setFullName(input.getFullName());
		user.setEmail(input.getEmail());
		user.setPassword(passwordEncoder.encode(input.getPassword()));
		user.setVerified(false);
		
		System.out.println("[debug] AuthenticationService::signup(ResgisterUserDto input)");
		
		if (userRepository.existsById(input.getUserId())) {
			throw new UsernameAlreadyExists("This user id " + input.getUserId() + " already exists");
		}
		
		// This is slow maybe <-----------------------------------------------------------------------------------------------[check]
		if (!userRepository.findByEmail(input.getEmail()).isEmpty()) {
			throw new UsernameAlreadyExists("This email " + input.getEmail() + " already exists");
		}
		
		user.setUserId(input.getUserId());
		
		return userRepository.save(user);
	}
	
	public User authenticate(LoginUserDto input) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
		System.out.println("[DEBUG]: AuthenticationService::authenticate(LoginUserDto input) called");
		User user = userRepository.findByEmail(input.getEmail()).orElseThrow();
		
		System.out.println("this email, " + user.getEmail() + "isVerified" + user.isVerified());
		
		// check if user is verified
		
		if (!user.isVerified()) {
			throw new UserNotVerifiedError("This user is not verified");
		}
		return user;
	}
}
