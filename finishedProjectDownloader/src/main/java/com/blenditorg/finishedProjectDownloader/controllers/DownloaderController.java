package com.blenditorg.finishedProjectDownloader.controllers;

import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blenditorg.finishedProjectDownloader.dtos.ProjectFileDto;
import com.blenditorg.finishedProjectDownloader.entities.ProjectFile;
import com.blenditorg.finishedProjectDownloader.repositories.ProjectFileRepository;
import com.blenditorg.finishedProjectDownloader.services.JwtService;
import com.blenditorg.finishedProjectDownloader.services.ZipFileGenerator;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/project")

public class DownloaderController {
	
	private final JwtService jwtService;
	private final ProjectFileRepository projectFileRepository;
	private final ZipFileGenerator zipFileGenerator;


	public DownloaderController(JwtService jwtService, ProjectFileRepository projectFileRepository,
			ZipFileGenerator zipFileGenerator) {
		super();
		this.jwtService = jwtService;
		this.projectFileRepository = projectFileRepository;
		this.zipFileGenerator = zipFileGenerator;
	}





	@PostMapping("/download")
	public ResponseEntity<Map<String, Object>> downloadFinishedProject(
			@RequestBody ProjectFileDto projectFileDto,
			HttpServletRequest request) {
		String token = request.getHeader("Authorization").substring(7);
		String userId = (String) jwtService.extractAllClaims(token).get("userId");
		
		ProjectFile projectFile = projectFileRepository.findByProjectId(projectFileDto.getProjectId()).get();
		
		System.out.println("Project Id: " + projectFile);
		
		if (!projectFile.isRenderingDone()) {
			return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
		}
		
		try {
			String url = zipFileGenerator.generateDownloadUrlForZip(userId, projectFile.getProjectId());
			System.out.println("presigned url" + url);
			return ResponseEntity.ok(Map.of("url", url));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
		}
		
	}
	
}
