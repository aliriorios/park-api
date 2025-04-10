package br.com.example.park_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class SpringCorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // Allowed URIs
                .allowedOrigins("*")        // Allowed domains or IP addresses
                .allowedHeaders("*")        // Allowed Headers HTTP
                .allowedMethods("*");       // Allowed Methods HTTP
    }
}
