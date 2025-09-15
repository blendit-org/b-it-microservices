package com.blenditorg.scoringSystem.services;

import org.springframework.stereotype.Service;

import com.blenditorg.scoringSystem.dtos.Work;
import com.blenditorg.scoringSystem.entities.User;
import com.blenditorg.scoringSystem.repositories.UserRepository;

@Service
public class ScoringService {
	
	public final UserRepository userRepository;

	public ScoringService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	public User scoreThis(String userId, Work work) {
		User user = userRepository.findByUserId(userId).get();
		
		long score = user.getScore() + (
				(work.getCpu() == 0 ? 1 : work.getCpu()) * 
				(work.getCpuPower() == 0 ? 1 : work.getCpuPower()) * 
				(work.getWorkTime() == 0 ? 1 : work.getWorkTime())) / 1000000;
		
		System.out.println("[Score]" + score);
		
		user.setScore(score);
		
		return userRepository.save(user);
	}
}
