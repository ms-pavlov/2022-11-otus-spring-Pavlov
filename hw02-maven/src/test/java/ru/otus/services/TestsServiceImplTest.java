package ru.otus.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.views.QuestionDto;
import ru.otus.views.QuestionFormatView;
import ru.otus.views.TestView;

import java.util.List;

import static org.mockito.Mockito.*;

class TestsServiceImplTest {
    private final static int CORRECT = 1;
    private final static QuestionDto QUESTION_DTO = new QuestionDto("?", List.of("1"), CORRECT-1);
    private final static String USER_NAME = "user";

    private QuestionService questionService;
    private QuestionFormatView questionFormatView;
    private TestView testView;

    @BeforeEach
    void setUp() {
        questionService = mock(QuestionService.class);
        questionFormatView = mock(QuestionFormatView.class);
        testView = mock(TestView.class);

        when(questionService.getQuestions()).thenReturn(List.of(QUESTION_DTO));
        when(testView.getUserAnswer()).thenReturn(CORRECT);
        when(testView.getUserName()).thenReturn(USER_NAME);
    }

    @Test
    void makeTests() {
        var testsService = new TestsServiceImpl(questionService, questionFormatView, testView);

        testsService.makeTests();

        verify(questionService, times(2)).getQuestions();
        verify(questionFormatView, times(1)).show(QUESTION_DTO);
        verify(testView, times(1)).getUserAnswer();
        verify(testView, times(1)).showResult(USER_NAME, 1, 0);
    }
}