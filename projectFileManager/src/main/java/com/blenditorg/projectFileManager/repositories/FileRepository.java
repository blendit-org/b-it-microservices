package com.blenditorg.projectFileManager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.blenditorg.projectFileManager.entities.ProjectFile;

@Repository
public interface FileRepository extends CrudRepository<ProjectFile, Long> {
	Optional<ProjectFile> findByProjectId(Long projectId);
	List<ProjectFile> findAllByUserId(String userId);
}
