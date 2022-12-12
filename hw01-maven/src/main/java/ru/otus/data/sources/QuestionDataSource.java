package ru.otus.data.sources;

import ru.otus.data.Question;

import java.util.List;

public interface QuestionDataSource {

    List<Question> getQuestions();

}
