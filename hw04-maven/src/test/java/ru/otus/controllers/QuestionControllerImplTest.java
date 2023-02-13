package ru.otus.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.messages.MessagesService;
import ru.otus.services.TestsService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionControllerImplTest {

    private final static String USER_NAME = "user";

    @MockBean
    private TestsService testsService;
    @MockBean
    MessagesService messagesService;

    @Test
    void showQuestions() {
        QuestionController controller = new QuestionControllerImpl(testsService, messagesService);

        controller.makeTestsAndDropName();

        verify(testsService, times(1)).makeTests();
    }

    @Test
    void setName() {
        QuestionController controller = new QuestionControllerImpl(testsService, messagesService);

        controller.seName(USER_NAME);

        verify(testsService, times(1)).setName(USER_NAME);
    }

    @Test
    void makeTestsAndDropName() {
        QuestionController controller = new QuestionControllerImpl(testsService, messagesService);

        controller.makeTestsAndDropName();

        verify(testsService, times(1)).makeTests();
        verify(testsService, times(1)).dropName();
    }
    @Test
    void checkIsNameSet() {
        QuestionController controller = new QuestionControllerImpl(testsService, messagesService);

        when(messagesService.getMessage("ask.name.unavailable")).thenReturn("ask.name.unavailable");

        assertFalse(controller.isNameSet().isAvailable());

        verify(messagesService, times(1)).getMessage("ask.name.unavailable");

        when(testsService.isNameSet()).thenReturn(true);

        assertTrue(controller.isNameSet().isAvailable());

    }


}