package com.blenditorg.projectStatusUpdate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blenditorg.projectStatusUpdate.entities.ProjectFile;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long> {

}
