package com.blenditorg.auth_api.configs;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.blenditorg.auth_api.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final HandlerExceptionResolver handlerExceptionResolver;

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(
			JwtService jwtService, 
			UserDetailsService userDetailsService,
			HandlerExceptionResolver handlerExceptionResolver
			) {
		
		System.out.println("[debug] JwtAuthenticationFilter() constructor called");
		
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
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
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			/**
			 * The Authorization header string looks like this:
			 * 
			 * 		"Bearer HEADER.PAYLOAD.SIGNATURE"
			 * 
			 * "Bearer " is 7 characters long and we need to strip that out
			 * so this uses authHeader.subsutring(7). So the string starts
			 * from the 8th character and looks like this:
			 * 
			 * 		"HEADER.PAYLOAD.SIGNATURE"
			 * this is the JWT token...
			 */
			final String jwt = authHeader.substring(7);
			final String email = jwtService.extractUsername(jwt); // extract email from JWT token
			System.out.println(email);

			// check if the token is already authorized. If true then skip to filterChain
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
            
            System.out.println("[debug] authentication = SecurityContextHolder.getContext().getAuthentication() " + authentication);

            if (email != null && authentication == null) {
            	// Load userDetails which belongs to the particular email from Database
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email); 
                
                // Check if the token is valid
                if (jwtService.isTokenValid(jwt, userDetails)) {
                	
                	// token valid: true -> generate an authentication token. Here, UsernamePasswordAuthenticationToken
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // add additional info to the token: remoteAddress and sessionId
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Finally get a SecurityContextHolder to hold the authToken
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
	}
	
}
