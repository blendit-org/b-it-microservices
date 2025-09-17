package com.blenditorg.auth_api.entities;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "users")
@Entity
public class User implements UserDetails{
	
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
	
//	@Column(nullable = false)
//	private boolean isVerified;
//	
//	
//	
//	public boolean isVerified() {
//		return isVerified;
//	}
//
//	public void setVerified(boolean isVerified) {
//		this.isVerified = isVerified;
//	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	/**
	 * User roles list. This [may be] useful for later
	 * design change
	 * @return the authorities, sorted by natural key (never <code>null</code>)
	 */
	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}
	
	@JsonIgnore
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public String getUsername() {
		return email;
	}
	
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
