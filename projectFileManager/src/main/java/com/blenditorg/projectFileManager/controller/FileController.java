package com.blenditorg.projectFileManager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blenditorg.projectFileManager.dtos.FileUploadDto;
import com.blenditorg.projectFileManager.entities.ProjectFile;
import com.blenditorg.projectFileManager.repositories.FileRepository;
import com.blenditorg.projectFileManager.services.FileRegisterService;
import com.blenditorg.projectFileManager.services.FileService;
import com.blenditorg.projectFileManager.services.JwtService;
import com.blenditorg.projectFileManager.services.ProjectJobPublisher;
import com.google.cloud.storage.HttpMethod;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/files")
public class FileController {
	
	private final FileService fileService;
	private final FileRegisterService fileRegisterService;
	private final ProjectJobPublisher projectJobPublisher;
	private final FileRepository fileRepository;
	private final JwtService jwtService;
	
	public FileController(FileService fileService, FileRegisterService fileRegisterService, ProjectJobPublisher projectJobPublisher, FileRepository fileRepository, JwtService jwtService) {
		this.fileService = fileService;
		this.fileRegisterService = fileRegisterService;
		this.projectJobPublisher = projectJobPublisher;
		this.fileRepository = fileRepository;
		this.jwtService = jwtService;
	}
	
	@PostMapping("/upload")
	public ResponseEntity<Map<String, Object>> generateUploadUrl(
			@RequestBody FileUploadDto fileUploadDto,
			HttpServletRequest request) {

		String userId = (String) request.getAttribute("userId");
		System.out.println("[fileUploadDto] " + fileUploadDto.getFileName() + " " + fileUploadDto.getStartFrame()+ " " + fileUploadDto.getEndFrame());
		
		
		ProjectFile projectFile = fileRegisterService.registerFileUnderUserId(fileUploadDto, userId);
		String objectPathName = projectFile.getUserId() + "/" + projectFile.getProjectId() + "/" + projectFile.getObjectPath();
		
		String url = fileService.generatePreSignedUrl(objectPathName, HttpMethod.PUT);
		
		projectJobPublisher.enqueueProject(projectFile.getProjectId(), projectFile.getUserId(), projectFile.getObjectPath() , projectFile.getStartFrame(), projectFile.getEndFrame());
				
		return ResponseEntity.ok(Map.of("url", url, "file", objectPathName));
	}
	
	// tested good
	@GetMapping("/all")
	public ResponseEntity<Map<String, Object>> getAllProjects(HttpServletRequest request) {
		
		String token = request.getHeader("Authorization").substring(7);
		String userId = (String) jwtService.extractAllClaims(token).get("userId");
		
		List<ProjectFile> allFiles = fileRepository.findAllByUserId(userId);
		
		return ResponseEntity.ok(Map.of(
				"files", allFiles));
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
