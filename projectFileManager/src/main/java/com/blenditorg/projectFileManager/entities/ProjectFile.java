package com.blenditorg.projectFileManager.entities;

import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "projectFiles")
@Entity
public class ProjectFile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long projectId;
	
	@Column(name = "userId")
	private String userId;
	
	@Column(name = "fileName")
	private String fileName;
	
	@JsonIgnore
	@Column(nullable = false)
	private String bucketName;
	
	@JsonIgnore
	@Column(nullable = false)
	private String objectPath;
	
	@Column(nullable = false)
	private Integer startFrame;
	
	@Column(nullable = false)
	private Integer endFrame;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Date createdAt;
	
	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean pushedIntoQueue;
	
	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean renderingDone;

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getObjectPath() {
		return objectPath;
	}

	public void setObjectPath(String objectPath) {
		this.objectPath = objectPath;
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

	public boolean isPushedIntoQueue() {
		return pushedIntoQueue;
	}

	public void setPushedIntoQueue(boolean pushedIntoQueue) {
		this.pushedIntoQueue = pushedIntoQueue;
	}

	public boolean isRenderingDone() {
		return renderingDone;
	}

	public void setRenderingDone(boolean renderingDone) {
		this.renderingDone = renderingDone;
	}
	
	
	
}
