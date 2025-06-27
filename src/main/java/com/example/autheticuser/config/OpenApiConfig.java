package com.example.autheticuser.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "AuthenticUser API", version = "v1", description = "API para Autenticação e Gerenciamento de Clientes e Produtos"))
@SecurityScheme(
    name = "bearerAuth", // Este nome deve ser o mesmo usado no @SecurityRequirement do controller
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfig {
}
