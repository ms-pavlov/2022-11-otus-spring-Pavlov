package ru.otus.controllers;

import ru.otus.services.QuestionService;
import ru.otus.views.QuestionFormatView;

public class QuestionControllerImpl implements QuestionController{

    private final QuestionService service;
    private final QuestionFormatView view;

    public QuestionControllerImpl(QuestionService service, QuestionFormatView view) {
        this.service = service;
        this.view = view;
    }

    @Override
    public void showQuestions() {
        view.show(service.getQuestions());
    }
}
