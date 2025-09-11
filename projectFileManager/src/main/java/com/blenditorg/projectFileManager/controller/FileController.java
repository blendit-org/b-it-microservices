package com.blenditorg.projectFileManager.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blenditorg.projectFileManager.dtos.FileUploadDto;
import com.blenditorg.projectFileManager.entities.ProjectFile;
import com.blenditorg.projectFileManager.services.FileRegisterService;
import com.blenditorg.projectFileManager.services.FileService;
import com.blenditorg.projectFileManager.services.ProjectJobPublisher;
import com.google.cloud.storage.HttpMethod;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/files")
public class FileController {
	
	private final FileService fileService;
	private final FileRegisterService fileRegisterService;
	private final ProjectJobPublisher projectJobPublisher;
	
	public FileController(FileService fileService, FileRegisterService fileRegisterService, ProjectJobPublisher projectJobPublisher) {
		this.fileService = fileService;
		this.fileRegisterService = fileRegisterService;
		this.projectJobPublisher = projectJobPublisher;
	}
	
	@PostMapping("/upload")
	public ResponseEntity<Map<String, Object>> generateUploadUrl(
			@RequestBody FileUploadDto fileUploadDto,
			HttpServletRequest request) {

		String userId = (String) request.getAttribute("userId");
		
		ProjectFile projectFile = fileRegisterService.registerFileUnderUserId(fileUploadDto, userId);
		String objectPathName = projectFile.getUserId() + "/" + projectFile.getProjectId() + "/" + projectFile.getObjectPath();
		
		String url = fileService.generatePreSignedUrl(objectPathName, HttpMethod.PUT);
		
		projectJobPublisher.enqueueProject(projectFile.getProjectId(), projectFile.getUserId(), projectFile.getObjectPath() , projectFile.getStartFrame(), projectFile.getEndFrame());
				
		return ResponseEntity.ok(Map.of("url", url, "file", objectPathName));
	}
	
	/**	
	@PostMapping("/download")
	public ResponseEntity<Map<String, Object>> generateDownloadUrl(
			@RequestBody FileUploadDto fileUploadDto,
			HttpServletRequest request) {
		String objectPathName = "Sholmiii_c6e61db0-1567-48cc-94ed-271dca02327a_testing.zip";
		
		String url = fileService.generatePreSignedUrl(objectPathName, HttpMethod.GET);
				
		return ResponseEntity.ok(Map.of("url", url, "file", objectPathName));
		
	}
	*/
	
}
