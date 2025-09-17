package com.blenditorg.renderProgressInfo.configurations;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfiguration {
	
	@Bean
	public CorsFilter corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		
		configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
		configuration.setAllowedOrigins(List.of("http://localhost:8005", "http://10.201.51.187:5173", "http://10.201.48.47:8005", "http://10.201.48.47:4000", "http://10.201.51.187:5173/render"));
		configuration.setAllowedHeaders(List.of("*"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		
		System.out.println("[debug] SecurityConfiguration::corsConfigurationSource() " + source);
		
		return new CorsFilter(source);
	}

}
