package com.blenditorg.auth_api.dtos;

public class VerificationRequestDto {
	private String email;

	public VerificationRequestDto(String email) {
		super();
		this.email = email;
	}

	public VerificationRequestDto() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
