package com.blenditorg.auth_api.configs;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	/**
	 * @pritomash hypothesis:
	 * AuthenticationProvider and JwtAuthenticationFilter gets auto-wired <-----------------[recheck] 
	 * 
	 */

	private final AuthenticationProvider authenticationProvider;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
			AuthenticationProvider authenticationProvider) {
		
		System.out.println("[debug] SecurityConfiguration() Constructor called.");
		
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.authenticationProvider = authenticationProvider;
	}

	/**
	 * Spring Security looks for a SecurityFilterChain bean during application startup.
	 * If it finds one, it uses that configuration instead of the default security setup.
	 * If none defined, Spring Security applies its own defaults.
	 * 
	 * <ul>
	 * <li>
	 * <b>.csrf()</b> 
	 * <a href="https://en.wikipedia.org/wiki/Cross-site_request_forgery">
	 * CSRF: Cross-site Request Forgery}</a> 
	 * is disabled. As this is a <i>STATELESS</i>
	 * application and no session cookies are stored.
	 * </li>
	 * 
	 * <li>
	 * <b>.authorizeHttpRequests()</b> 
	 * allows "/auth/**" endpoints to be accessible to everyone (login/register).
	 * For any other endpoints an authentication is required.
	 * </li>
	 * 
	 * <li>
	 * <b> .sessionManagement() </b>
	 * sets the session creation policy to STATELESS
	 * </li>
	 * 
	 * <li>
	 * <b> .authenticationProvider() </b>
	 * the AuthenticationProvider bean was declared in {@link com.blenditorg.auth_api.configs.ApplicationConfiguration#authenticationProvider() ApplicationConfiguration}
	 *  
	 * </li>
	 * 
	 * <li>
	 * <b> .addFilterBefore() </b>
	 * ensures the JwtAuthenticationFilter pass is before the UsernamePasswordAuthenticationFilter pass
	 * </li>
	 * 
	 * </ul>
	 * 
	 * @param http
	 * @return SecurityFilterChain Bean
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf) -> csrf.disable())
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests((authorizeHttpRequests) -> 
					authorizeHttpRequests
						.requestMatchers("/auth/**", "/public/**")
						.permitAll()
						.anyRequest()
						.authenticated())
				.sessionManagement((sessionManagement) -> 
					sessionManagement
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		System.out.println("[debug] SecurityConfiguration::securityFilterChain() ");
		
		return http.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		
		configuration.setAllowedMethods(List.of("GET", "POST"));
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		
		System.out.println("[debug] SecurityConfiguration::corsConfigurationSource() " + source);
		
		return source;
	}

}
