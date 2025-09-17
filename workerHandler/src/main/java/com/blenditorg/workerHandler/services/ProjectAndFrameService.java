package com.blenditorg.workerHandler.services;

import java.util.Optional;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import com.blenditorg.workerHandler.configurations.RabbitConfig;
import com.blenditorg.workerHandler.dtos.FrameMessage;
import com.blenditorg.workerHandler.entities.ProjectAndFrame;
import com.blenditorg.workerHandler.repositories.ActiveProjects;

@Service
public class ProjectAndFrameService {
	
	private final ActiveProjects activeProjects;
	private final AmqpTemplate amqpTemplate;

	public ProjectAndFrameService(ActiveProjects activeProjects, AmqpTemplate amqpTemplate) {
		super();
		this.activeProjects = activeProjects;
		this.amqpTemplate = amqpTemplate;
	}
	
	public ProjectAndFrame registerNewProjectAndFrame(Long projectId, Integer frame) {
		System.out.println("repo change.");
		if (activeProjects.existsByProjectIdAndFrame(projectId, frame)) {
			System.out.println("exist by: " + projectId + frame);
			System.out.println("Entity: " + activeProjects.findByProjectIdAndFrame(projectId, frame).get());
			return activeProjects.findByProjectIdAndFrame(projectId, frame).get();
		}
		
		ProjectAndFrame job = new ProjectAndFrame();
		
		job.setProjectId(projectId);
		job.setFrame(frame);
		
		return activeProjects.save(job);
	}
	
	// DANGER:
	/**
	 * 
	 * @param jobId
	 * @param projectId
	 * @param frame
	 * @return
	 * 
	 * `true` if successfully deleted
	 * `false` means either this job was not registered or already done
	 */
	public boolean dropRenderedProjectAndFrame(Long jobId, Long projectId, Integer frame) {
		Optional<ProjectAndFrame> job = activeProjects.findByJobId(jobId);
		
		if (job != null) {
			activeProjects.delete(job.get());
			return true;
		}
		
		return false;
	}
	
	public ProjectAndFrame updateRenderedStatusToTrue(Long jobId) {
		Optional<ProjectAndFrame> job = activeProjects.findByJobId(jobId);
		
		if (job != null) {
			ProjectAndFrame updateThisJob = job.get();
			updateThisJob.setRendered(true);
			return activeProjects.save(updateThisJob);
		}
		return null;
	}
	
	public void sendRenderStats(FrameMessage msg) {
		amqpTemplate.convertAndSend(RabbitConfig.RENDER_STATS_QUEUE, msg);
	}
}
