package ru.otus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Locale;

@Configuration
@PropertySource("application.yml")
public class AppPropertiesConfig {

    @Value("${application.locale}")
    private Locale locale;

    @Bean
    public Locale locale() {
        return locale;
    }

}
