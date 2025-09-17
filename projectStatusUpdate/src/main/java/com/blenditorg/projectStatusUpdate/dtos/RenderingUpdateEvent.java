package com.blenditorg.projectStatusUpdate.dtos;

public class RenderingUpdateEvent {
    private Long projectId;

    public RenderingUpdateEvent() {
		super();
	}
    
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
