package com.blenditorg.auth_api.exceptions;

public class UserNotVerifiedError extends RuntimeException {

	public UserNotVerifiedError(String message) {
		super(message);
	}
	
}
