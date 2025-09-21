package com.blenditorg.auth_api.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionsHandler {
	
	private ProblemDetail errorDetail = null;
	
	@ExceptionHandler(BadCredentialsException.class)
	public ProblemDetail handleBadCredentialsException(BadCredentialsException exception) {
		errorDetail = ProblemDetail
				.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
		errorDetail.setProperty("description", "The username or password is invalid");
		return errorDetail;
	}
	
	@ExceptionHandler(AccountStatusException.class)
	public ProblemDetail handleAccountStatusException(AccountStatusException exception) {
		errorDetail = ProblemDetail
				.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
		errorDetail.setProperty("description", "The account is locked");
		return errorDetail;
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ProblemDetail handleAccessDeniedException(AccessDeniedException exception) {
		errorDetail = ProblemDetail
				.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
		errorDetail.setProperty("description", "You are not authorized to access this resource");
		return errorDetail;
	}
	
	@ExceptionHandler(SignatureException.class)
	public ProblemDetail handleSignatureException(SignatureException exception) {
		errorDetail = ProblemDetail
				.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
		errorDetail.setProperty("description", "The JWT signature is invalid");
		return errorDetail;
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ProblemDetail handleExpiredJwtException(ExpiredJwtException exception) {
		errorDetail = ProblemDetail
				.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
		errorDetail.setProperty("description", "The JWT token is expired");
		return errorDetail;
	}
	
	@ExceptionHandler(UsernameAlreadyExists.class)
	public ProblemDetail handleUsernameAlreadyExists(UsernameAlreadyExists exception) {
		errorDetail = ProblemDetail
				.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
		errorDetail.setProperty("description", "Try another username...");
		return errorDetail;
	}
	
	@ExceptionHandler(UserNotVerifiedError.class)
	public ProblemDetail handleUserNotVerifiedError(UsernameAlreadyExists exception) {
		errorDetail = ProblemDetail
				.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
		errorDetail.setProperty("description", "This user is not verified");
		return errorDetail;
	}
	
	@ExceptionHandler(VerificationMismatch.class)
	public ProblemDetail handleVerificationMismatch(UsernameAlreadyExists exception) {
		errorDetail = ProblemDetail
				.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
		errorDetail.setProperty("description", "Verification code does not match");
		return errorDetail;
	}
	
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleAllOtherExceptions(Exception exception) {
		errorDetail = ProblemDetail
				.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
		errorDetail.setProperty("description", "Internal server error");
		return errorDetail;
	}
	
	
}
