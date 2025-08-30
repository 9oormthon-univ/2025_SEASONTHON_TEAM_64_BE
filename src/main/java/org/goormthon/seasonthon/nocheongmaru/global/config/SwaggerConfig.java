package org.goormthon.seasonthon.nocheongmaru.global.config;

import io.swagger.v3.oas.models.OpenAPI;

import org.goormthon.seasonthon.nocheongmaru.global.swagger.OpenApiConfigurer;
import org.goormthon.seasonthon.nocheongmaru.global.swagger.SwaggerErrorExampleGenerator;
import org.goormthon.seasonthon.nocheongmaru.global.swagger.SwaggerOperationCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Value("${spring.swagger.base-url}")
    private String baseUrl;
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenApiConfigurer(baseUrl).createOpenAPI();
    }
    
    @Bean
    public OperationCustomizer customize() {
        return new SwaggerOperationCustomizer(
            new SwaggerErrorExampleGenerator()
        );
    }
    
}