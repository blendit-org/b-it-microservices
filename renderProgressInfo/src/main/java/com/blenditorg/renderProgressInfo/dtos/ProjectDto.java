package com.blenditorg.renderProgressInfo.dtos;

public class ProjectDto {
	private long projectId;
	private int startFrame;
	private int endFrame;
	

	public int getStartFrame() {
		return startFrame;
	}

	public void setStartFrame(int startFrame) {
		this.startFrame = startFrame;
	}

	public int getEndFrame() {
		return endFrame;
	}

	public void setEndFrame(int endFrame) {
		this.endFrame = endFrame;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public long getProjectId() {
		return projectId;
	}
}
