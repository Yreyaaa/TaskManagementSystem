package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;


@Configuration
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(
        info = @Info(
                title = "Task Management System Application Api",
                description = "Loyalty System", version = "1.0.0",
                contact = @Contact(
                        name = "Yuri",
                        email = "aamrazr@gmail.com",
                        url = "aamrazr@gmail.com"
                )
        )
)
class SwaggerConfig {
//    @Bean
//    Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.controllers"))
//                .paths(PathSelectors.any())
//                .build();
//    }
}