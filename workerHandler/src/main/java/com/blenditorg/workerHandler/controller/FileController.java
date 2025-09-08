package com.blenditorg.workerHandler.controller;


import java.util.Map;

import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blenditorg.workerHandler.configurations.RabbitConfig;
import com.blenditorg.workerHandler.services.FileService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;


@RestController
@RequestMapping("/api/worker")
public class FileController {
	
	private final Channel channel;
	private final FileService fileService;
	
	
	public FileController(
			ConnectionFactory factory,
			FileService fileService,
			ObjectMapper objectMapper) throws Exception {
		Connection connection = factory.createConnection();
		this.channel = connection.createChannel(false);
		this.fileService = fileService;
		
		channel.queueDeclare(RabbitConfig.PROJECT_QUEUE, true, false, false, null);

	}
	
	@PostMapping("/job-request")
	public ResponseEntity<Map<String, Object>> generateUploadUrl() throws Exception {
		
		// Fetch one message from queue
		GetResponse response = channel.basicGet(RabbitConfig.PROJECT_QUEUE, false);
		
		System.out.println("queue response: " + response);
		
		if (response == null) {
			return ResponseEntity.ok(Map.of("status", "no-job-found"));
		}
//		System.out.println(response.getBody());
//		String msgBody = new String(response.getBody(), StandardCharsets.UTF_8);
//		System.out.println("msgBody: " + msgBody);
//		Map<String, Object> job = objectMapper.readValue(response.getBody(), Map.class);
//		
//		
//		Long projectId = ((Number) job.get("projectId")).longValue();
//		String userId = (String) job.get("userId");
//		String fileName = (String) job.get("fileName");
//		Integer frame = ((Number) job.get("frame")).intValue();
//		
		
		ObjectMapper objectMapper = new ObjectMapper();
		TypeReference<Map<String, Object>> typrRef = new TypeReference<Map<String,Object>>() {
		};
		Map<String, Object> job = objectMapper.readValue(response.getBody(), typrRef);
		
		Long projectId = ((Number) job.get("projectId")).longValue();
		String userId = (String) job.get("userId");
		String fileName = (String) job.get("fileName");
		Integer frame = ((Number) job.get("frame")).intValue();
		
		System.out.println("Info: " + projectId + userId + fileName + frame);
		
		String url = fileService.generateGetPresignedUrl(userId + "/" + projectId + "/" + fileName);
				
		return ResponseEntity.ok(Map.of(
				"status", "successs",
				"url", url,
				"projectId", projectId,
				"userId", userId,
				"frame", frame));
	}
	
}
