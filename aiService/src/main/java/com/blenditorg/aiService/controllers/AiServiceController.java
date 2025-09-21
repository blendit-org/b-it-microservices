package com.blenditorg.aiService.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blenditorg.aiService.dtos.GenerateRequest;
import com.blenditorg.aiService.dtos.GenerateResponse;
import com.blenditorg.aiService.service.JobService;

@RestController
@RequestMapping("/api")
public class AiServiceController {
	
	private final JobService jobService;
	public AiServiceController(JobService jobService) { this.jobService = jobService; }
	
	@PostMapping("/generate")
	public ResponseEntity<GenerateResponse> generate(@RequestBody GenerateRequest request) {
		String res = jobService.generateBlendFile(request.getPrompt());
		return ResponseEntity.ok(new GenerateResponse(res));
	}
}
