package com.blenditorg.workerHandler.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "ongoingRenders")
@Entity
public class ProjectAndFrame {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long jobId;
	
	@Column(nullable = false)
	private long projectId;
	
	@Column(nullable = false)
	private int frame;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Date createdAt;
	
	@Column(columnDefinition = "boolean default false")
	private boolean rendered;

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
		
}
