package ru.otus.controllers;

import org.springframework.stereotype.Component;
import ru.otus.services.TestsService;

@Component
public class QuestionControllerImpl implements QuestionController {

    private final TestsService testsService;


    public QuestionControllerImpl(TestsService testsService) {
        this.testsService = testsService;
    }

    @Override
    public void makeTests() {
        testsService.makeTests();
    }
}
