package com.blenditorg.auth_api.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.blenditorg.auth_api.entities.User;
import com.blenditorg.auth_api.repositories.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<User> allUsers() {
		List<User> users = new ArrayList<>();
		
		userRepository.findAll().forEach(users::add);
		
		return users;
	}
}
