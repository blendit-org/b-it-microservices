package com.blenditorg.auth_api.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.blenditorg.auth_api.repositories.UserRepository;

@Configuration
public class ApplicationConfiguration {
	private final UserRepository userRepository;
	
	public ApplicationConfiguration(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	/**
	 * <p>
	 * 		{@link org.springframework.security.core.userdetails.UserDetailsService UserDetailsService}
	 * 		is an interface in Spring Security with one method:
	 * </p>
	 * <code>UserDetails loadUserByUsername(String username) throws UsernameNotFoundException</code>
	 * <p>
	 * 		<b>userDetailsService()</b> returns a <b>lambda</b> that overrides that method.
	 * 		So this basically runs a query in the database and returns a
	 * 		{@link org.springframework.security.core.userdetails.UserDetails UserDetails}
	 * 		type instance.
	 * </p>
	 * @return
	 */
	@Bean
	UserDetailsService userDetailsService() {
		return username -> userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	/**
	 * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider.DaoAuthenticationProvider 
	 * DaoAuthenticaitonProvider()} is deprecated. It suggests to provide the UserDetailsService
	 * <b>DaoAuthenticaitonProvider(UserDetailsService userDetailsService)</b>
	 * 
	 * @return 
	 */
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
}
