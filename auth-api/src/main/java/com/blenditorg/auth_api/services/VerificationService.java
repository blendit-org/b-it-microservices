package com.blenditorg.auth_api.services;

import org.springframework.stereotype.Service;

import com.blenditorg.auth_api.entities.User;
import com.blenditorg.auth_api.exceptions.VerificationMismatch;
import com.blenditorg.auth_api.repositories.UserRepository;

@Service
public class VerificationService {
	
	private final UserRepository userRepository;
	
	private final MailService mailService;
	
	

	public VerificationService(UserRepository userRepository, MailService mailService) {
		super();
		this.userRepository = userRepository;
		this.mailService = mailService;
	}
	
	public User verificationCodeMatches(String email, long verificationCode) {
		User user = userRepository.findByEmail(email).get();
		
		if (user.getVerificationCode() == verificationCode) {
			user.setVerified(true);
			userRepository.save(user);
			return user;
		} else {
			throw new VerificationMismatch("Verification code mismatch");
		}
	}
	
	public void verificationCodeSent(String email) {
		User user = userRepository.findByEmail(email).get();
		long verificationCode = 100000 + (long) (Math.random() * (999999-100000));
		user.setVerificationCode(verificationCode);
		userRepository.save(user);
		mailService.sendMessageWithInputStreamAttachment(user.getEmail(), verificationCode);
		
	}
}
