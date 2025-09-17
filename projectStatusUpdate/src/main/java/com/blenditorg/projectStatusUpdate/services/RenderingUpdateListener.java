package com.blenditorg.projectStatusUpdate.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.blenditorg.projectStatusUpdate.dtos.RenderingUpdateEvent;
import com.blenditorg.projectStatusUpdate.repositories.ProjectFileRepository;

@Service
public class RenderingUpdateListener {

	private final ProjectFileRepository projectFileRepo;

    public RenderingUpdateListener(ProjectFileRepository projectFileRepo) {
        this.projectFileRepo = projectFileRepo;
    }

    @RabbitListener(queues = "${app.queue.name}")
    public void handleRenderingUpdate(RenderingUpdateEvent event) {
        System.out.println("UpdateService received update: " + event);

        projectFileRepo.findById(event.getProjectId()).ifPresent(project -> {
            project.setRenderingDone(true);
            projectFileRepo.save(project);
        });
    }
}
