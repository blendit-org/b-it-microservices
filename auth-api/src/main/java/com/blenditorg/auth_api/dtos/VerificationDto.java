package com.blenditorg.auth_api.dtos;

public class VerificationDto {
	private long verificationCode;
	private String email;
	public long getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(long verificationCode) {
		this.verificationCode = verificationCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public VerificationDto(long verificationCode, String email) {
		super();
		this.verificationCode = verificationCode;
		this.email = email;
	}
}
