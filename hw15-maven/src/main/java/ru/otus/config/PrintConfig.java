package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Function;

@Configuration
public class PrintConfig {
    @Bean
    public Function<Date, String> dateFormat() {
        return date -> new SimpleDateFormat("dd MMMM yyyy hh:mm", Locale.getDefault())
                .format(date);
    }

    @Bean
    public PrintStream printStream() {
        return System.out;
    }


}
