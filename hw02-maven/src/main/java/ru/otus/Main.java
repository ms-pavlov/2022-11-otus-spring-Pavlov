package ru.otus;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.controllers.QuestionController;

@Configuration
@ComponentScan
public class Main {

    public static void main(String... args) throws RuntimeException {
        AnnotationConfigApplicationContext context =  new AnnotationConfigApplicationContext(Main.class);
        QuestionController controller = context.getBean(QuestionController.class);
        controller.makeTests();
    }
}
