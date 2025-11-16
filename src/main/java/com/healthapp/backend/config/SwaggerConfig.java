package com.healthapp.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration for API documentation.
 * Auto-detects Railway production environment and configures HTTPS server URL.
 * Accessible at /swagger-ui/index.html
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.forward-headers-strategy:framework}")
    private String forwardHeadersStrategy;

    /**
     * Configures OpenAPI documentation with JWT Bearer authentication scheme.
     * Automatically uses HTTPS in production (Railway) and HTTP in local development.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        // Determine if we're in production (Railway uses RAILWAY_ENVIRONMENT variable)
        String railwayEnv = System.getenv("RAILWAY_ENVIRONMENT");
        String serverUrl = railwayEnv != null
            ? "https://healthservicesapp-production.up.railway.app"
            : "http://localhost:8080";

        Server server = new Server()
                .url(serverUrl)
                .description(railwayEnv != null ? "Production server" : "Local development server");

        return new OpenAPI()
                .addServersItem(server)
                .info(new Info()
                        .title("HealthApp API")
                        .description("Healthcare Platform API - Milestone 1: Authentication")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("HealthApp Team")
                                .email("support@healthapp.com")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}