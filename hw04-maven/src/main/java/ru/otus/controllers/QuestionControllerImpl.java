package ru.otus.controllers;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;
import ru.otus.services.TestsService;

@ShellComponent
public class QuestionControllerImpl implements QuestionController {

    private final TestsService testsService;

    public QuestionControllerImpl(TestsService testsService) {
        this.testsService = testsService;
    }

    @Override
    @ShellMethod(value = "Set User Name Command", key = {"n", "name"})
    public void seName(String name) {
        testsService.setName(name);
    }

    @Override
    @ShellMethod(value = "Make Test for current user")
    public void makeTestsAndDropName() {
        testsService.makeTests();
        testsService.dropName();
    }
}
