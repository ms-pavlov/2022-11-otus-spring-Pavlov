package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.services.TestsService;

@ShellComponent
public class QuestionControllerImpl implements QuestionController {

    private final TestsService testsService;

    @Autowired
    public QuestionControllerImpl(TestsService testsService) {
        this.testsService = testsService;
    }

    @Override
    @ShellMethod(value = "Set User Name Command", key = {"n", "name"})
    public void seName(@ShellOption(defaultValue = "AnyUser") String name) {
        testsService.setName(name);
    }

    @Override
    @ShellMethod(value = "Make Test for current user", key = {"test", "makeTest"})
    public void makeTestsAndDropName() {
        testsService.makeTests();
        testsService.dropName();
    }
}
