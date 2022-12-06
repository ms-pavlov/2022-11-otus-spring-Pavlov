package ru.otus.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.otus.services.QuestionService;
import ru.otus.views.QuestionDto;
import ru.otus.views.QuestionFormatView;

import java.util.List;

import static org.mockito.Mockito.*;

class QuestionControllerImplTest {

    private QuestionService service;
    private QuestionFormatView view;

    @BeforeEach
    void setUp() {
        service = mock(QuestionService.class);
        view = mock(QuestionFormatView.class);
    }

    @Test
    void showQuestions() {
        QuestionController controller = new QuestionControllerImpl(service, view);
        ArgumentCaptor<List<QuestionDto>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        controller.showQuestions();

        verify(service, times(1)).getQuestions();

        verify(view, times(1)).show(argumentCaptor.capture());
    }
}