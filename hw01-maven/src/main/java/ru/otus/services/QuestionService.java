package ru.otus.services;

import ru.otus.views.QuestionDto;

import java.util.List;

public interface QuestionService {

    List<QuestionDto> getQuestions();

    int[] getAnswers();
}
