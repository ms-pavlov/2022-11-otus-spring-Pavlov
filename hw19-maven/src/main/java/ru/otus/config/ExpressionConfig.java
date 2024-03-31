package ru.otus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.expressions.ExpressionFactory;

import java.util.function.Function;

@Configuration
public class ExpressionConfig {

    @Autowired
    private ApplicationContext context;

    @Bean
    public Function<String, ExpressionFactory> expressionFactory() {
        return name -> context.getBean(name, ExpressionFactory.class);
    }
}
