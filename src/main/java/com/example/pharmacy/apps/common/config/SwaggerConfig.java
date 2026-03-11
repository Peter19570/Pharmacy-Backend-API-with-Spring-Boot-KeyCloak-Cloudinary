package com.example.pharmacy.apps.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Pharmacy")
                        .description("Pharmacy Backend API to manage drugs at {store-name}")
                        .contact(new Contact()
                                .name("Peter Nwaogu")
                                .email("bobstone195712345@gmail.com")));
    }
}
