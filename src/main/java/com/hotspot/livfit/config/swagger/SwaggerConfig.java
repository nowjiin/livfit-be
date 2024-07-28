package com.hotspot.livfit.config.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
  @Value("${server.servlet.context-path:}")
  private String contextPath;

  @Bean
  public OpenAPI customOpenAPI() {
    Server localServer = new Server();
    localServer.setUrl(contextPath);
    localServer.setDescription("Local Server");

    Server prodServer = new Server();
    prodServer.setUrl("https://nowjin.tplinkdns.com");
    prodServer.setDescription("Production Server");

    return new OpenAPI()
        .addServersItem(localServer)
        .addServersItem(prodServer)
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(
            new Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
        .info(
            new Info()
                .title("livfit API")
                .version("1.0")
                .description(
                    "API Test용 ID : test_dev Token : eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ0ZXN0X2RldiIsImlhdCI6MTcyMjE5MTMwMCwiZXhwIjoxNzIyNzk2MTAwfQ.E1F54pI5EtWtqkOeSQgy8wy_H76opHpUv9JZkvBSIgg"));
  }

  @Bean
  public GroupedOpenApi customGroupedOpenApi() {
    return GroupedOpenApi.builder().group("api").pathsToMatch("/**").build();
  }
}
