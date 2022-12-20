package ru.otus.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

@Configuration
@PropertySource("application.yml")
public class AppConfiguration {

    private final static PrintStream SYSTEM_OUT = System.out;
    private final static InputStream SYSTEM_IN = System.in;

    @Bean
    public PrintStream printStream() {
        return SYSTEM_OUT;
    }

    @Bean
    public Scanner getScanner() {
        return new Scanner(SYSTEM_IN);
    }

    @Bean
    public String path(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage("path", null, locale);
    }
}
