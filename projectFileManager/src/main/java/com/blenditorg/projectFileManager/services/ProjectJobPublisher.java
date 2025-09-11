package com.blenditorg.projectFileManager.services;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.blenditorg.projectFileManager.configurations.RabbitConfig;

@Service
public class ProjectJobPublisher {
	
	private final RabbitTemplate rabbitTemplate;

	public ProjectJobPublisher(RabbitTemplate rabbitTemplate) {
		super();
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void enqueueProject(Long projectId, String userId, String objectPath, int startFrame, int endFrame) {
		for (int frame = startFrame; frame <= endFrame; frame++) {
			Map<String, Object> job = Map.of(
					"userId", userId,
					"projectId", projectId,
					"fileName", objectPath,
					"frame", frame
			);
			rabbitTemplate.convertAndSend(RabbitConfig.PROJECT_QUEUE, job);
			System.out.println("Pushed Job: "+ job);
		}
	}
	
}
