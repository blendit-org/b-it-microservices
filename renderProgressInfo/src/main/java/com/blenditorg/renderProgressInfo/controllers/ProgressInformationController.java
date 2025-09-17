package com.blenditorg.renderProgressInfo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blenditorg.renderProgressInfo.dtos.ProjectDto;
import com.blenditorg.renderProgressInfo.dtos.RenderingUpdateEvent;
import com.blenditorg.renderProgressInfo.entities.ProjectAndFrame;
import com.blenditorg.renderProgressInfo.repositories.ProjectFrameRepository;
import com.blenditorg.renderProgressInfo.services.FileService;
import com.blenditorg.renderProgressInfo.services.JwtService;
import com.blenditorg.renderProgressInfo.services.ProjectJobDonePublisher;

@RequestMapping("/project")
@RestController
public class ProgressInformationController {
	
	
	private final ProjectFrameRepository projectFrameRepository;
	private final ProjectJobDonePublisher projectJobDonePublisher;
//	private FileService fileService;
//	private JwtService jwtService;
	
	public ProgressInformationController(ProjectFrameRepository projectFrameRepository, ProjectJobDonePublisher projectJobDonePublisher, FileService fileService, JwtService jwtService) {
		super();
		this.projectFrameRepository = projectFrameRepository;
		this.projectJobDonePublisher = projectJobDonePublisher;
//		this.fileService = fileService;
//		this.jwtService = jwtService;
	}


	@GetMapping("/status/frames-rendered")
	public ResponseEntity<Map<String, Object>> framesRenderedForThisProject(
			@RequestBody ProjectDto projectDto) {
		List<ProjectAndFrame> renderedFrames = projectFrameRepository.findByProjectIdAndRendered(projectDto.getProjectId(), true);
		int numberOfRenderedFrames = renderedFrames.size();
		int totalFrames = projectDto.getEndFrame() - projectDto.getStartFrame() + 1;
		
		System.out.println("[rendered-frames]: " + numberOfRenderedFrames + " [total-frames]" + totalFrames);
		
		// if number of rendered frames is equal total frame +- 5
		// push to rabbitmq
		if ((totalFrames - numberOfRenderedFrames) < 2 ) {
			RenderingUpdateEvent finishedProject = new RenderingUpdateEvent();
			finishedProject.setProjectId(projectDto.getProjectId());
			projectJobDonePublisher.enqueueProject(finishedProject);
		}
		
		return ResponseEntity.ok(Map.of(
				"renderedFrames", numberOfRenderedFrames,
				"totalFrames", totalFrames));
	}
	
//	@PostMapping("/download")
//	public ResponseEntity<Map<String, Object>> downloadProject(
//			@RequestBody ProjectDto projectDto,
//			HttpServletRequest request) {
//		String token = request.getHeader("Authorization").substring(7);
//		String userId = (String) jwtService.extractAllClaims(token).get("userId");
//		String pathToRenderedImages = userId + "/" + projectDto.getProjectId() + "/rendered";
//		String url = fileService.generateGetPresignedUrl(pathToRenderedImages);
//		
//		return ResponseEntity.ok(Map.of(
//				"url", url));
//	}
	
	
}
