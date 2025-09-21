package com.blenditorg.auth_api.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blenditorg.auth_api.repositories.UserRepository;

@RestController
@RequestMapping("/public")
public class PublicInfo {
	
	public final UserRepository userRepository;
	
	public PublicInfo(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@GetMapping("/")
	public ResponseEntity<Map<String, Long>> getTotalUsers() {
		long count = userRepository.count();
		return ResponseEntity.ok(Map.of("totalUsers", count));
	}
}
