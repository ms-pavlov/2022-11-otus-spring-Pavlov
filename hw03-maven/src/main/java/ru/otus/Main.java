package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.controllers.QuestionController;

@SpringBootApplication
public class Main {

    public static void main(String... args) throws RuntimeException {
        var context = SpringApplication.run(Main.class, args);
        QuestionController controller = context.getBean(QuestionController.class);
        controller.makeTests();
    }
}
