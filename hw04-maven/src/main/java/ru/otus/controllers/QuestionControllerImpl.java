package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.messages.MessagesService;
import ru.otus.services.TestsService;

import java.util.Optional;

@ShellComponent
public class QuestionControllerImpl implements QuestionController {

    private final TestsService testsService;
    private final MessagesService messagesService;

    @Autowired
    public QuestionControllerImpl(
            TestsService testsService,
            MessagesService messagesService) {
        this.testsService = testsService;
        this.messagesService = messagesService;
    }

    @Override
    @ShellMethod(value = "Set User Name Command", key = {"n", "name"})
    public void seName(@ShellOption(defaultValue = "AnyUser") String name) {
        testsService.setName(name);
    }

    @Override
    @ShellMethod(value = "Make Test for current user", key = {"test", "makeTest"})
    @ShellMethodAvailability({"isNameSet"})
    public void makeTestsAndDropName() {
        testsService.makeTests();
        testsService.dropName();
    }

    @Override
    public Availability isNameSet() {
        return Optional.ofNullable(testsService)
                .filter(TestsService::isNameSet)
                .map(tests -> Availability.available())
                .orElseGet(() -> Availability.unavailable(messagesService.getMessage("ask.name.unavailable")));
    }
}
