package com.blenditorg.workerHandler.configurations;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.blenditorg.workerHandler.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final HandlerExceptionResolver handlerExceptionResolver;

	private final JwtService jwtService;

	public JwtAuthenticationFilter(
			JwtService jwtService, 
			HandlerExceptionResolver handlerExceptionResolver
			) {
		
		System.out.println("[debug] JwtAuthenticationFilter() constructor called");
		
		this.jwtService = jwtService;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}
	
	/**
	 * This method overrides the OncePerRequestFilter's doFilterInternal(request, response, filterChain)
	 * The main goal here is to Create a spring SecurityContext for a request. If the request is
	 * valid - means it has a authHeader and JWT token - then this
	 * method proceeds to authenticate the token. If the request is not valid
	 * then the filterChain.doFilter(request, response) is called and the 
	 * method is returned there. This will return the control to the Controller.
	 * 
	 * If the request is valid then the authentication process starts. It checks
	 * if the JWT token is valid. If valid it stores authentication token inside
	 * SecurityContextHolder. SecurityContextHolder gets reset after each request.
	 * 
	 * The request is state-less, meaning every request must be treated as a new one, 
	 * even if it comes from the same client or has been received earlier.
	 */
	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain
			) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization"); // retrieve the header "Authorization"
		System.out.println(authHeader);
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			System.out.println("[debug] first filter chain: no Bearer token");
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			String token = authHeader.substring(7);
			if (!jwtService.isTokenValid(token)) {			
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			
			String userId = jwtService.extractAllClaims(token).get("userId", String.class);
			request.setAttribute("userId", userId);
			filterChain.doFilter(request, response);
			
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
	}
	
}
