package com.blenditorg.projectFileManager.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.blenditorg.projectFileManager.dtos.FileUploadDto;
import com.blenditorg.projectFileManager.entities.ProjectFile;
import com.blenditorg.projectFileManager.repositories.FileRepository;


@Service
public class FileRegisterService {
	
	@Value("${gcs.BucketName}")
	private String bucketName;
	
	private FileRepository fileRepository;
	
	public FileRegisterService(FileRepository fileRepository, JwtService jwtService) {
		this.fileRepository = fileRepository;
	}
	
	public ProjectFile registerFileUnderUserId(FileUploadDto fileUploadDto, String userId) {
		ProjectFile projectFile = new ProjectFile();
		
		System.out.println("registerFileUnderUserId(): userId -> " + userId);
		
		projectFile.setFileName(fileUploadDto.getFileName());
		projectFile.setUserId(userId);
		projectFile.setStartFrame(fileUploadDto.getStartFrame());
		projectFile.setEndFrame(fileUploadDto.getEndFrame());
		projectFile.setBucketName(bucketName);
		projectFile.setObjectPath(userId + "_" + UUID.randomUUID() + "_" + projectFile.getFileName());
		
		System.out.println("registerFileUnderUserId(): userId -> " + projectFile + " " + projectFile.getProjectId());
		
		return fileRepository.save(projectFile);
	}
}
