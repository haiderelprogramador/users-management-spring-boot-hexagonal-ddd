package com.jcaa.usersmanagement.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Documentacion OpenAPI. Declara el esquema "bearerAuth" (JWT) para que Swagger UI muestre el
 * boton "Authorize" y envie el header Authorization: Bearer &lt;token&gt; en cada peticion.
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Users Management API", version = "v1"),
    security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT")
public class OpenApiConfig {}
