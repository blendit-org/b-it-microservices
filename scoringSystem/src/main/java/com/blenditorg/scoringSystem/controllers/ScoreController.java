package com.blenditorg.scoringSystem.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blenditorg.scoringSystem.dtos.Work;
import com.blenditorg.scoringSystem.entities.User;
import com.blenditorg.scoringSystem.services.ScoringService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/score")
public class ScoreController {
	
	private final ScoringService scoringService;

	public ScoreController(ScoringService scoringService) {
		super();
		this.scoringService = scoringService;
	}
	
	@PostMapping("/worker")
	public ResponseEntity<Map<String, Object>> scoreWorker(
			@RequestBody Work work ,
			HttpServletRequest request
			) {
		String userId = (String) request.getAttribute("userId");
		User user = scoringService.scoreThis(userId, work);
		
		return ResponseEntity.ok(Map.of(
				"user", user
		));
	}
}
