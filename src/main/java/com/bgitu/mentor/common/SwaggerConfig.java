package com.bgitu.mentor.common;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api(){
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server().url("http://localhost:8080")
                                //new Server().url("http://89.22.225.116:8080")
                        )
                )
                .info(
                        new Info().title("API for task-tracker")
                );
    }
}
