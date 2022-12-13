package ru.otus.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.services.TestsService;

import static org.mockito.Mockito.*;

class QuestionControllerImplTest {

    private TestsService testsService;
    @BeforeEach
    void setUp() {
        testsService = mock(TestsService.class);
    }

    @Test
    void showQuestions() {
        QuestionController controller = new QuestionControllerImpl(testsService);

        controller.makeTests();

        verify(testsService, times(1)).makeTests();
    }
}