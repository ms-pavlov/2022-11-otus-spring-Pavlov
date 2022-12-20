package ru.otus.views;

import java.util.List;

public interface QuestionFormatView {

    void show(QuestionDto question);

    void show(List<QuestionDto> questions);
}
