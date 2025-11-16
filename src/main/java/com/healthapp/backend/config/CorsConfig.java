package com.healthapp.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * CORS (Cross-Origin Resource Sharing) configuration for frontend integration.
 * Allows requests from localhost:3000 (patient frontend) and localhost:3001 (doctor frontend).
 */
@Configuration
public class CorsConfig {

    /**
     * Configures CORS settings for all endpoints (/**).
     * Allows common HTTP methods and headers for API communication.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow requests from frontend development servers
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",  // Patient frontend
                "http://localhost:3001",  // Doctor frontend
                "http://localhost:3002",  // Admin frontend (if needed)
                "*"                       // Allow all origins (use with caution in production)
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all headers (includes Authorization)
        configuration.setAllowCredentials(true); // Allow cookies and credentials
        configuration.setMaxAge(3600L); // Cache preflight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints

        return source;
    }
}