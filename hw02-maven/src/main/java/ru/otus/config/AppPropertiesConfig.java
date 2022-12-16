package ru.otus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@PropertySource("application.properties")
public class AppPropertiesConfig {

    @Bean
    public Map<String,String> properties(
            @Value("${test.ask.answer}") String ask,
            @Value("${test.ask.name}") String name,
            @Value("${test.result}") String result,
            @Value("${resource.path}") String path) {
        return Map.of("result", result,
                "ask.answer", ask,
                "ask.name", name,
                "path", path);
    }

}
