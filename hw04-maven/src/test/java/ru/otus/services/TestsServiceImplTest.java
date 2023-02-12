package ru.otus.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.views.QuestionDto;
import ru.otus.views.QuestionFormatView;
import ru.otus.views.TestView;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class TestsServiceImplTest {
    private final static int CORRECT = 1;
    private final static QuestionDto QUESTION_DTO = new QuestionDto("?", List.of("1"), CORRECT-1);
    private final static String USER_NAME = "user";

    private final static String SPACE_NAME = "                  \r\n";

    @MockBean
    private QuestionService questionService;
    @MockBean
    private QuestionFormatView questionFormatView;
    @MockBean
    private TestView testView;

    @BeforeEach
    void setUp() {
        when(questionService.getQuestions()).thenReturn(List.of(QUESTION_DTO));
        when(testView.getUserAnswer()).thenReturn(CORRECT);
        when(testView.getUserName()).thenReturn(USER_NAME);
    }

    @Test
    void makeTestsCorrect() {
        var testsService = new TestsServiceImpl(questionService, questionFormatView, testView);

        testsService.setName(USER_NAME);
        testsService.makeTests();

        verify(questionService, times(2)).getQuestions();
        verify(questionFormatView, times(1)).show(QUESTION_DTO);
        verify(testView, times(1)).showResult(USER_NAME, 1, 0);
    }

    @Test
    void makeTestsError() {
        var testsService = new TestsServiceImpl(questionService, questionFormatView, testView);
        when(testView.getUserAnswer()).thenReturn(CORRECT + 100);

        testsService.setName(USER_NAME);

        assertTrue(testsService.isNameSet());

        testsService.makeTests();

        verify(questionService, times(2)).getQuestions();
        verify(questionFormatView, times(1)).show(QUESTION_DTO);
        verify(testView, times(1)).showResult(USER_NAME, 1, 1);
    }

    @Test
    void setName() {
        var testsService = new TestsServiceImpl(questionService, questionFormatView, testView);
        when(testView.getUserAnswer()).thenReturn(CORRECT + 100);

        testsService.setName(USER_NAME);
        testsService.makeTests();

        verify(testView, times(1)).showResult(USER_NAME, 1, 1);
    }

    @Test
    void dropName() {
        var testsService = new TestsServiceImpl(questionService, questionFormatView, testView);

        testsService.setName(USER_NAME);
        testsService.dropName();

        assertFalse(testsService.isNameSet());
    }

    @Test
    void isNameSet() {
        var testsService = new TestsServiceImpl(questionService, questionFormatView, testView);

        testsService.setName(USER_NAME);

        assertTrue(testsService.isNameSet());

        testsService.setName(null);

        assertFalse(testsService.isNameSet());

        testsService.setName(SPACE_NAME);

        assertFalse(testsService.isNameSet());

    }
}