package ru.otus.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Configuration
public class JacksonConfig {

    @Bean
    public DateFormat dateFormat() {
        return new SimpleDateFormat("dd MMMM yyyy hh:mm", Locale.getDefault());
    }
    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }
}
