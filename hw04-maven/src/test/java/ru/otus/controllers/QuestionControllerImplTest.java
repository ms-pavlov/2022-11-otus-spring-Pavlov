package ru.otus.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.services.TestsService;

import static org.mockito.Mockito.*;

@SpringBootTest
class QuestionControllerImplTest {

    private final static String USER_NAME = "user";

    @MockBean
    private TestsService testsService;

    @Test
    void showQuestions() {
        QuestionController controller = new QuestionControllerImpl(testsService);

        controller.makeTestsAndDropName();

        verify(testsService, times(1)).makeTests();
    }

    @Test
    void seName() {
        QuestionController controller = new QuestionControllerImpl(testsService);

        controller.seName(USER_NAME);

        verify(testsService, times(1)).setName(USER_NAME);
    }

    @Test
    void makeTestsAndDropName() {
        QuestionController controller = new QuestionControllerImpl(testsService);

        controller.makeTestsAndDropName();

        verify(testsService, times(1)).makeTests();
        verify(testsService, times(1)).dropName();
    }
}