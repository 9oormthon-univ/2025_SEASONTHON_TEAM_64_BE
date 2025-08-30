package org.goormthon.seasonthon.nocheongmaru.global.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class OpenApiConfigurer {
    
    private static final String AUTH_NAME = "Json Web Token";
    private static final String LOCAL_SERVER_URL = "http://localhost:8080";
    private static final String API_TITLE = "DeerForYou API Document";
    private static final String API_VERSION = "v0.0.1";
    private static final String API_DESCRIPTION = "DeerForYou API 문서";
    private static final String BEARER_TOKEN_DESCRIPTION = "Access Token 토큰을 입력해주세요.(Bearer X)";
    
    private final String baseUrl;
    
    public OpenApiConfigurer(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public OpenAPI createOpenAPI() {
        return new OpenAPI()
            .addSecurityItem(createSecurityRequirement())
            .components(createComponents())
            .info(createApiInfo())
            .servers(createServerList());
    }
    
    private Info createApiInfo() {
        return new Info()
            .title(API_TITLE)
            .version(API_VERSION)
            .description(API_DESCRIPTION);
    }
    
    private SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement().addList(AUTH_NAME);
    }
    
    private Components createComponents() {
        return new Components()
            .addSecuritySchemes(AUTH_NAME, createSecurityScheme());
    }
    
    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
            .name(AUTH_NAME)
            .type(SecurityScheme.Type.HTTP)
            .scheme("Bearer")
            .bearerFormat("JWT")
            .description(BEARER_TOKEN_DESCRIPTION);
    }
    
    private List<Server> createServerList() {
        return List.of(
            createProductionServer(),
            createDevelopmentServer()
        );
    }
    
    private Server createProductionServer() {
        return new Server()
            .description("Production Server")
            .url(baseUrl);
    }
    
    private Server createDevelopmentServer() {
        return new Server()
            .description("Development Server")
            .url(LOCAL_SERVER_URL);
    }
    
}