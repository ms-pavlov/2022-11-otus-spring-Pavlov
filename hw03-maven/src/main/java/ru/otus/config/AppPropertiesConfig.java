package ru.otus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Locale;

@Configuration
@PropertySource("application.yml")
public class AppPropertiesConfig {

    @Value("${application.locale}")
    private Locale locale;

    @Value("${application.path}")
    private String path;

    @Bean
    public Locale locale() {
        return locale;
    }

    @Bean
    public String path() {
        return path;
    }
}
