package com.blenditorg.renderProgressInfo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blenditorg.renderProgressInfo.entities.ProjectAndFrame;

@Repository
public interface ProjectFrameRepository extends JpaRepository<ProjectAndFrame, Long>{
	public List<ProjectAndFrame> findByProjectIdAndRendered(Long projectId, boolean rendered); 
}
