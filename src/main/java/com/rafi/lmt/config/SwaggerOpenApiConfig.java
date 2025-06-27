// src/main/java/com/rafi/lmt/config/OpenApiConfig.java
package com.rafi.lmt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerOpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LMT Queue API")
                        .version("1.0")
                        .description("API documentation for LMT Queue System By Rafi"))
                .tags(Arrays.asList(
                        new Tag().name("ProvisionSystem").description("Provision system operations"),
                        new Tag().name("PosQSystem").description("POS queue operations"),
                        new Tag().name("PrinterGatewaySystem").description("Printer gateway operations")
                ));
    }
}