package com.blenditorg.scoringSystem.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blenditorg.scoringSystem.entities.User;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByUserId(String userId);
}
