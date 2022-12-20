package ru.otus.data.storages;

import ru.otus.data.Question;

import java.util.List;

public interface QuestionDataStorage {

    Question find(int index);

    int getCount();

    List<Question> findAll();

    List<Integer> getTemplate();
}
