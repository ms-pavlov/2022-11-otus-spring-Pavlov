package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.views.QuestionFormatView;
import ru.otus.views.TestView;

@Service
public class TestsServiceImpl implements TestsService {
    private final QuestionService questionService;
    private final QuestionFormatView questionFormatView;
    private final TestView testView;

    public TestsServiceImpl(QuestionService questionService, QuestionFormatView questionFormatView, TestView testView) {
        this.questionService = questionService;
        this.questionFormatView = questionFormatView;
        this.testView = testView;
    }

    @Override
    public void makeTests() {
        String name = testView.getUserName();
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
