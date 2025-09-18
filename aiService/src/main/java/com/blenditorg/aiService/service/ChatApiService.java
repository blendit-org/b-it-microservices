package com.blenditorg.aiService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.blenditorg.aiService.dtos.ChatRequest;
import com.blenditorg.aiService.dtos.ChatResponse;

@Service
public class ChatApiService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	private final String API_KEY = "sk-x4HpP3H8vTv6zITUrhQpy69LmCmqqNd6obaAcYkwKnzEXBlb";
	
	private static final String API_URL = "https://api.chatanywhere.tech/v1/chat/completions";

	public ChatResponse callChatApi(ChatRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(API_KEY);
		
		System.out.println(headers);
		
		HttpEntity<ChatRequest> entity = new HttpEntity<ChatRequest>(request, headers);
		
		ResponseEntity<ChatResponse> response = restTemplate.exchange(
				API_URL, 
				HttpMethod.POST,
				entity,
				ChatResponse.class);
		return response.getBody();
	}
}
