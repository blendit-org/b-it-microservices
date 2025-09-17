package com.blenditorg.finishedProjectDownloader.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blenditorg.finishedProjectDownloader.entities.ProjectFile;

@Repository
public interface ProjectFileRepository  extends JpaRepository<ProjectFile, Long>{
	Optional<ProjectFile> findByProjectId(long projectId);
}
