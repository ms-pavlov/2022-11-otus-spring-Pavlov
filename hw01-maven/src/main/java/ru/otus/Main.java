package ru.otus;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.controllers.QuestionController;

public class Main {

    public static void main(String... args) throws RuntimeException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("./spring-context.xml");
        QuestionController controller = context.getBean("questionController", QuestionController.class);

        controller.showQuestions();
    }
}
