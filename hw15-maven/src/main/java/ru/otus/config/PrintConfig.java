package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.PrintStream;

@Configuration
public class PrintConfig {

    @Bean
    public PrintStream printStream() {
        return System.out;
    }


}
