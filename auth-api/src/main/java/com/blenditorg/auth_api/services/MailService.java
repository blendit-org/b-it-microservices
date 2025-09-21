package com.blenditorg.auth_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	
	@Autowired
	private JavaMailSender emailSender;

	public void sendMessageWithInputStreamAttachment(
	  String to, Long verificaionCode) {
	    try {
			MimeMessage message = emailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        helper.setFrom("noreply@baeldung.com");
	        helper.setTo(to);
	        helper.setSubject("blend:it verification code");
	        helper.setText(generateText(verificaionCode));

	        emailSender.send(message);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	}

	private String generateText(Long verificaionCode) {
		return "Your blend:it verification code is " + verificaionCode;
	}
}
