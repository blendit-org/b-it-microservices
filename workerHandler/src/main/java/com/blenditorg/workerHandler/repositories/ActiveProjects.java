package com.blenditorg.workerHandler.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.repository.CrudRepository;

import com.blenditorg.workerHandler.entities.ProjectAndFrame;

public interface ActiveProjects extends JpaRepository<ProjectAndFrame, Long> {
	Optional<ProjectAndFrame> findByJobId(Long jobId);
	Optional<ProjectAndFrame> findByProjectIdAndFrame(long projectId, int frame);
	
	boolean existsByProjectIdAndFrame(long projectId, int frame);
}
