package ru.otus.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.services.TestsService;

import static org.mockito.Mockito.*;

@SpringBootTest
class QuestionControllerImplTest {

    @MockBean
    private TestsService testsService;

    @Test
    void showQuestions() {
        QuestionController controller = new QuestionControllerImpl(testsService);

        controller.makeTests();

        verify(testsService, times(1)).makeTests();
    }
}