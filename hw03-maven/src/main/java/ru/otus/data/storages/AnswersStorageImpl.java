package ru.otus.data.storages;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnswersStorageImpl implements AnswersStorage {
    private final List<Integer> answers;

    public AnswersStorageImpl(QuestionDataStorage questionDataStorage) {
        answers = new ArrayList<>();
        for (int i = 0; i < questionDataStorage.getTemplate().size(); i++) {
            answers.add(i, (int) (Math.random() * (questionDataStorage.getTemplate().get(i) + 1)));
        }
    }

    @Override
    public List<Integer> getAnswers() {
        return answers;
    }

}
