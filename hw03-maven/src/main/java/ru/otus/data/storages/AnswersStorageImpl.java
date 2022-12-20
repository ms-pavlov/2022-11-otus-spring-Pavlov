package ru.otus.data.storages;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnswersStorageImpl implements AnswersStorage{
    private final List<Integer> answers;

    public AnswersStorageImpl(QuestionDataStorage questionDataStorage) {
        answers = getNewAnswers(questionDataStorage);
    }

    @Override
    public List<Integer> getAnswers() {
        return answers;
    }

    private List<Integer> getNewAnswers(QuestionDataStorage questionDataStorage) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < questionDataStorage.getTemplate().size(); i++) {
            result.add(i, getRandomInteger(questionDataStorage.getTemplate().get(i)));
        }
        return result;
    }

    private static int getRandomInteger(int max){
        return (int)(Math.random()*(max+1));
    }
}
