package com.blenditorg.renderProgressInfo.services;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.blenditorg.renderProgressInfo.configurations.RabbitConfig;
import com.blenditorg.renderProgressInfo.dtos.RenderingUpdateEvent;

@Service
public class ProjectJobDonePublisher {
	
	private final RabbitTemplate rabbitTemplate;

	public ProjectJobDonePublisher(RabbitTemplate rabbitTemplate) {
		super();
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void enqueueProject(RenderingUpdateEvent renderingDoneMessage) {
		rabbitTemplate.convertAndSend(RabbitConfig.PROJECT_QUEUE, renderingDoneMessage);
		System.out.println("Pushed Job: "+ renderingDoneMessage);
	}
	
}
