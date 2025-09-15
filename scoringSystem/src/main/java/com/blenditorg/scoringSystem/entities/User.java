package com.blenditorg.scoringSystem.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "users")
@Entity
public class User {
	
	@Id
	@Column(unique = true, nullable = false)
	private String userId;
	
	@Column(nullable = false)
	private String fullName;
	
	@Column(unique = true, length = 100, nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@CreationTimestamp
	@Column(updatable = false, name = "created_at")
	private Date createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private Date updatedAt;
	
	@Column(nullable = false)
	private long score;
	
	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	
	public String getPassword() {
		return password;
	}
	
	public String getUsername() {
		return email;
	}

	public String getUserId() {
		return userId;
	}


	public String getFullName() {
		return fullName;
	}


	public String getEmail() {
		return email;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public Date getUpdatedAt() {
		return updatedAt;
	}

	
}
