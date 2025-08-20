package com.blenditorg.auth_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blenditorg.auth_api.dtos.LoginUserDto;
import com.blenditorg.auth_api.dtos.RegisterUserDto;
import com.blenditorg.auth_api.entities.User;
import com.blenditorg.auth_api.responses.LoginResponse;
import com.blenditorg.auth_api.services.AuthenticationService;
import com.blenditorg.auth_api.services.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	
	private final JwtService jwtService;
	
	private final AuthenticationService authenticationService;
	
	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
		this.jwtService = jwtService;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
		User registeredUser = authenticationService.signup(registerUserDto);
		
		return ResponseEntity.ok(registeredUser);
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
		User authenticatedUser = authenticationService.authenticate(loginUserDto);
		
		String jwtToken = jwtService.generateToken(authenticatedUser);
		System.out.println(jwtService.extractUsername(jwtToken));
		
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());
		
		return ResponseEntity.ok(loginResponse);
	}
}
