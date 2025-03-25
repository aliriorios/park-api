package br.com.example.park_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {
    @Bean
    public OpenAPI openAPI () {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Security", securityScheme()))
                .info(
                new Info()
                        .title("REST API - Spring Park")
                        .description("API for vehicle parking management")
                        .version("v1")
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .contact(new Contact().name("Alírio Rios").email("contato.alirio@gmail.com"))
        );
    }

    // Enabling a field to insert the JWT Token into Swagger-ui
    private SecurityScheme securityScheme () {
        return new SecurityScheme()
                .description("Enter a valid bearer token to proceed")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Security");
    }
}
