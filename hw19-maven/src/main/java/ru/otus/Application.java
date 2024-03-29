package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableReactiveMongoRepositories
@SpringBootApplication
@EnableWebFlux
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
