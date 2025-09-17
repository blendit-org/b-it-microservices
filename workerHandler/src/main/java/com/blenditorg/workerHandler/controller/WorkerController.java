package com.blenditorg.workerHandler.controller;


import java.time.LocalDate;
import java.util.Map;

import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blenditorg.workerHandler.configurations.RabbitConfig;
import com.blenditorg.workerHandler.dtos.FrameMessage;
import com.blenditorg.workerHandler.dtos.JobFileMetadata;
import com.blenditorg.workerHandler.entities.ProjectAndFrame;
import com.blenditorg.workerHandler.services.FileService;
import com.blenditorg.workerHandler.services.ProjectAndFrameService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/worker")
public class WorkerController {
	
	private final Channel channel;
	
	private final FileService fileService;
	
	private final ProjectAndFrameService projectAndFrameService;
	
	
	public WorkerController(
			ConnectionFactory factory,
			FileService fileService,
			ObjectMapper objectMapper,
			ProjectAndFrameService projectAndFrameService) throws Exception {
		Connection connection = factory.createConnection();
		this.channel = connection.createChannel(false);
		this.fileService = fileService;
		this.projectAndFrameService = projectAndFrameService;
		
		channel.queueDeclare(RabbitConfig.PROJECT_QUEUE, true, false, false, null);

	}
	
	@PostMapping("/job-request")
	public ResponseEntity<Map<String, Object>> generateUploadUrl(HttpServletRequest request) throws Exception {
		
		// Fetch one message from queue
		GetResponse response = channel.basicGet(RabbitConfig.PROJECT_QUEUE, false);
		
		System.out.println("queue response: " + response);
		
		if (response == null) {
			return ResponseEntity.notFound().build();
		}
//		System.out.println(response.getBody());
//		String msgBody = new String(response.getBody(), StandardCharsets.UTF_8);
//		System.out.println("msgBody: " + msgBody);
//		Map<String, Object> job = objectMapper.readValue(response.getBody(), Map.class);	
		
		ObjectMapper objectMapper = new ObjectMapper();
		TypeReference<Map<String, Object>> typrRef = new TypeReference<Map<String,Object>>() {
		};
		Map<String, Object> job = objectMapper.readValue(response.getBody(), typrRef);
		
		Long projectId = ((Number) job.get("projectId")).longValue();
		String userId = (String) job.get("userId");
		String fileName = (String) job.get("fileName");
		Integer frame = ((Number) job.get("frame")).intValue();
		
		System.out.println("Info: " + projectId + userId + fileName + frame);
		
		// get userId: [in this context userId is workerId] from JWT key
		String workerId = (String) request.getAttribute("userId");
		
		// work-in-progess project-frame pushed to repository
		ProjectAndFrame registeredJob = projectAndFrameService.registerNewProjectAndFrame(projectId, frame);
		
		// ack immediately after getting Info and registering to repository ^
		channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
		
		System.out.println("acking done... " + workerId);
		
		String url = fileService.generateGetPresignedUrl(userId + "/" + projectId + "/" + fileName);
		
				
		return ResponseEntity.ok(Map.of(
				"status", "success",
				"url", url,
				"jobId", registeredJob.getJobId(),
				"projectId", projectId,
				"userId", userId,
				"frame", frame,
				"fileName", fileName));
	}
	
	@GetMapping("/image-upload")
	public ResponseEntity<Map<String, Object>> getImageUploadUrl(
			@RequestBody JobFileMetadata jobFileMetadata) throws Exception {
		// String workerId = jobFileMetadata.getWorkerId();
		String userId = jobFileMetadata.getUserId();
		Long projectId = jobFileMetadata.getProjectId();
		String fileName = jobFileMetadata.getFileName(); // frame#####.png
		// int frame = jobFileMetadata.getFrame();
		// Long jobId = jobFileMetadata.getJobId();
		
		/**
		 * TODO:
		 * 1. Update Active Project Repository of this project and frame to SUCCESS
		 * 2. LATER: Push into RabbitMQ queue <- this queue is for updating Status of a
		 * 	  Project and Later, marking a project as done. This is handled in another
		 * 	  micro-service projectStatusUpdate
		 * 3. Update worker score.
		 * 4. Response with an upload url for uploading frame
		 */
		
		// 1. Update Active Project Repository of this project and frame to SUCCESS
		//    Currently just update the rendered to true
		// projectAndFrameService.updateRenderedStatusToTrue(jobId);

		
		// 4. Response with an upload url for uploading frame
		String objectPath = userId + "/" + projectId + "/rendered/" + fileName;
		
		String url = fileService.generatePutPresignedUrl(objectPath);
		
		return ResponseEntity.ok(Map.of("url", url));
	}
	
	@PostMapping("/job-success")
	public ResponseEntity<String> jobSuccessfullyDone(
			@RequestBody JobFileMetadata jobFileMetadata) throws Exception {
		Long jobId = jobFileMetadata.getJobId();
		
		// update the job as done...
		projectAndFrameService.updateRenderedStatusToTrue(jobId);
		projectAndFrameService.sendRenderStats(new FrameMessage(LocalDate.now()));
		return ResponseEntity.ok("Job Uploaded Successfully");
	}
	
}
