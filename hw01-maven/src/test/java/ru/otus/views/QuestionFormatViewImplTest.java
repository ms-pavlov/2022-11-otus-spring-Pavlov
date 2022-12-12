package ru.otus.views;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.util.List;

import static org.mockito.Mockito.*;

class QuestionFormatViewImplTest {

    private final static List<QuestionDto> QUESTIONS_LIST = List.of(
            new QuestionDto("2 x 2 = ?", List.of("=3", "=4", "=5"), 1)
    );

    private PrintStream printStream;

    @BeforeEach
    void setUp() {
        printStream = mock(PrintStream.class);
    }

    @Test
    void testShow() {

        QuestionFormatView view = new QuestionFormatViewImpl(printStream);

        view.show(QUESTIONS_LIST.get(0));

        verify(printStream, times(1)).println(QUESTIONS_LIST.get(0).getText());

        var decisions = QUESTIONS_LIST.get(0).getDecisions();

        for (int i = 0; i < decisions.size(); i++) {
            verify(printStream, times(1)).println((i + 1) + ". " + decisions.get(i));
        }

    }

    @Test
    void testShowAll() {
        QuestionFormatView view = new QuestionFormatViewImpl(printStream);

        view.show(QUESTIONS_LIST);

        verify(printStream, times(1)).println(QUESTIONS_LIST.get(0).getText());

        var decisions = QUESTIONS_LIST.get(0).getDecisions();

        for (int i = 0; i < decisions.size(); i++) {
            verify(printStream, times(1)).println((i + 1) + ". " + decisions.get(i));
        }
    }
}