package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.views.QuestionFormatView;
import ru.otus.views.TestView;

import java.util.Optional;

@Service
public class TestsServiceImpl implements TestsService {
    private final QuestionService questionService;
    private final QuestionFormatView questionFormatView;
    private final TestView testView;

    private String name;

    public TestsServiceImpl(QuestionService questionService, QuestionFormatView questionFormatView, TestView testView) {
        this.questionService = questionService;
        this.questionFormatView = questionFormatView;
        this.testView = testView;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void dropName() {
        this.name = null;
    }

    @Override
    public boolean isNameSet() {
        return Optional.ofNullable(name)
                .map(s -> !s.isBlank())
                .orElse(false);
    }

    @Override
    public void makeTests() {
        int errors = 0;
        for (var question : questionService.getQuestions()) {
            questionFormatView.show(question);
            if (!question.checkAnswer(testView.getUserAnswer() - 1)) {
                errors++;
            }
        }

        testView.showResult(name, questionService.getQuestions().size(), errors);
    }
}
