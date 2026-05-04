package com.example.demo.conf;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8085}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Система учета движения деталей API")
                        .version("1.0.0")
                        .description("""
                                API для автоматизации учета движения деталей в учебных мастерских техникума.
                                
                                ## Основные возможности:
                                * Управление справочником деталей (CRUD)
                                * Создание и согласование заявок
                                * Складской учет (приемка, выдача, списание)
                                * Формирование отчетности и аналитика
                                * Аутентификация и авторизация через JWT
                                """)
                        .contact(new Contact()
                                .name("Студент группы ИСП-9-2")
                                .email("NeDam@example.com")
                                .url("https://github.com/Pavel200-7"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Локальный сервер разработки")
                ));
    }
}
