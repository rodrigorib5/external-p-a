package com.empresa.producta.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Order API")
            .version("1.0.0")
            .description("API for order management")
            .license(new License().name("Apache 2.0").url("https://springdoc.org")));
  }

  @Bean
  public GroupedOpenApi ordersApi() {
    return GroupedOpenApi.builder()
        .group("Orders API")
        .packagesToScan("com.empresa.producta.controller")
        .build();
  }
}
