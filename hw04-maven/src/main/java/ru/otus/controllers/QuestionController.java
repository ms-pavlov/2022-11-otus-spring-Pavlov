package ru.otus.controllers;

import org.springframework.shell.Availability;

public interface QuestionController {

    void seName(String name);

    void makeTestsAndDropName();

    Availability isNameSet();
}
