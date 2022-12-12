package ru.otus.services;

import ru.otus.data.storages.AnswersStorage;
import ru.otus.data.storages.QuestionDataStorage;
import ru.otus.views.QuestionDto;

import java.util.ArrayList;
import java.util.List;

public class QuestionServiceImpl implements QuestionService{

    private final QuestionDataStorage dataStorage;
    private final AnswersStorage answersStorage;

    public QuestionServiceImpl(QuestionDataStorage questionDataStorage, AnswersStorage answersStorage) {
        this.dataStorage = questionDataStorage;
        this.answersStorage = answersStorage;
    }

    @Override
    public List<QuestionDto> getQuestions() {
        List<QuestionDto> result = new ArrayList<>();
        for (int i = 0; i < dataStorage.getCount(); i++) {
            result.add(new QuestionDto(dataStorage.find(i), getAnswers()[i]));
        }
        return result;
    }

    @Override
    public int[] getAnswers() {
        return answersStorage.getAnswers();
    }
}
