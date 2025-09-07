package com.blenditorg.projectFileManager.dtos;

public class FileUploadDto {
	private String fileName;
	private Integer startFrame;
	private Integer endFrame;
	
	
	
	public FileUploadDto(String fileName, Integer startFrame, Integer endFrame) {
		this.fileName = fileName;
		this.startFrame = startFrame;
		this.endFrame = endFrame;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getStartFrame() {
		return startFrame;
	}
	public void setStartFrame(Integer startFrame) {
		this.startFrame = startFrame;
	}
	public Integer getEndFrame() {
		return endFrame;
	}
	public void setEndFrame(Integer endFrame) {
		this.endFrame = endFrame;
	}
	
	
}
