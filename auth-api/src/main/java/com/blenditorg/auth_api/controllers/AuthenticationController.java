package com.blenditorg.auth_api.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blenditorg.auth_api.dtos.LoginUserDto;
import com.blenditorg.auth_api.dtos.RegisterUserDto;
import com.blenditorg.auth_api.dtos.VerificationDto;
import com.blenditorg.auth_api.dtos.VerificationRequestDto;
import com.blenditorg.auth_api.entities.User;
import com.blenditorg.auth_api.responses.LoginResponse;
import com.blenditorg.auth_api.services.AuthenticationService;
import com.blenditorg.auth_api.services.JwtService;
import com.blenditorg.auth_api.services.MailService;
import com.blenditorg.auth_api.services.VerificationService;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	
	private final JwtService jwtService;
	
	private final AuthenticationService authenticationService;
	
	private final VerificationService verificationService;
	
	//private final MailService mailService;
	
	
	
//	@PostMapping("/signup")
//	public ResponseEntity<Map<String, Object>> signup(@RequestBody RegisterUserDto registerUserDto) {
//		System.out.println("[debug] /auth/signup");
//		
//		String emailAuthToken = jwtService.buildEmailToken(registerUserDto);
//		String url = "http://localhost:8005";
//		
//		return ResponseEntity.ok(Map.of(
//				"message", "There is a link sent to your email. Click it to verify"));
//		
//	}
	
	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService,
			VerificationService verificationService) {
		super();
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
		this.verificationService = verificationService;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
		
		System.out.println("[debug] /auth/signup/email-verification");
		
		User registeredUser = authenticationService.signup(registerUserDto);
		
		return ResponseEntity.ok(registeredUser);
	}
	
	

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
		
		System.out.println("[debug] /auth/login");
		
		User authenticatedUser = authenticationService.authenticate(loginUserDto);
		
		// user needs to be email verified
//		if (!authenticatedUser.isVerified()) {
//			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//		}
//		
		// Building JWT token with email and userId information
		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("userId", authenticatedUser.getUserId());
		
		
		String jwtToken = jwtService.generateToken(extraClaims, authenticatedUser);
		System.out.println(jwtService.extractUsername(jwtToken));
		
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());
		
		return ResponseEntity.ok(loginResponse);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		System.out.println("[logout] "+ token);
		// blacklist token
		
		return ResponseEntity.ok("Logged out successfully");
	}
	
	@PostMapping("/verify")
	public ResponseEntity<Map<String, Object>> verificationRequest(@RequestBody VerificationRequestDto verificationDto) {
		verificationService.verificationCodeSent(verificationDto.getEmail());
		return ResponseEntity.ok(Map.of("message", "Verification code sent"));
	}
	
	@PostMapping("/verify/confirm")
	public ResponseEntity<Map<String, Object>> verifyEmail(@RequestBody VerificationDto verificationDto) {
		verificationService.verificationCodeMatches(verificationDto.getEmail(), verificationDto.getVerificationCode());
		return ResponseEntity.ok(Map.of("message", "Verification code matches"));
	}
	
}
