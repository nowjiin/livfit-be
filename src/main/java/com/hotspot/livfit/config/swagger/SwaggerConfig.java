package com.hotspot.livfit.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springdoc.core.models.GroupedOpenApi;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;

// 스웨거 접속 주소
// http://localhost:8080/swagger-ui.index.html

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("livfit API")
                                .version("1.0")
                                .description("livfit API description"));
    }

    @Bean
    public GroupedOpenApi customGroupedOpenApi() {
        return GroupedOpenApi.builder().group("api").pathsToMatch("/**").build();
    }
}
