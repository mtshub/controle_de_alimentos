package com.senai.controle_de_alimentos.infrastructure.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI oficinaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Mercadinho diSkina")
                        .description("Cadastro e gestão de alimentos do mercadinho.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipe diSkina")
                                .email("suportedi@skina.com"))
                );
    }
}