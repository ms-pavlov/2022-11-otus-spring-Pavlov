package ru.otus.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.data.Question;
import ru.otus.data.sources.QuestionDataSource;
import ru.otus.data.storages.AnswersStorage;
import ru.otus.data.storages.QuestionDataStorage;
import ru.otus.data.storages.QuestionDataStorageImpl;
import ru.otus.views.QuestionDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QuestionServiceImplTest {
    private final static List<Question> QUESTIONS_LIST = List.of(
            new Question("2 x 2 = ?", List.of("3", "5"), "4"),
            new Question("7 x 7 = ?", List.of("47", "77"), "49"),
            new Question("8 x 8 = ?", List.of("68", "88"), "64"),
            new Question("4 x 4 = ?", List.of("14", "44"), "16"),
            new Question("11 x 11 = ?", List.of("111", "1111"), "121")
    );

    private final static List<QuestionDto> EXPECTATIONS = List.of(
            new QuestionDto("2 x 2 = ?", List.of("3", "4", "5"), 1),
            new QuestionDto("7 x 7 = ?", List.of("47", "49", "77"), 1),
            new QuestionDto("8 x 8 = ?", List.of("68", "64", "88"), 1),
            new QuestionDto("4 x 4 = ?", List.of("14", "16", "44"), 1),
            new QuestionDto("11 x 11 = ?", List.of("111", "121", "1111"), 1)
    );

    private final static int[] ANSWERS = {1, 1, 1, 1, 1};


    private QuestionDataStorage dataStorage;
    private AnswersStorage answersStorage;

    @BeforeEach
    void setUp() {
        var dataSource = mock(QuestionDataSource.class);
        when(dataSource.getQuestions()).thenReturn(QUESTIONS_LIST);
        dataStorage = new QuestionDataStorageImpl(dataSource);

        answersStorage = mock(AnswersStorage.class);
        when(answersStorage.getAnswers()).thenReturn(ANSWERS);
    }

    @Test
    void getQuestions() {
        QuestionService service = new QuestionServiceImpl(dataStorage, answersStorage);

        assertEquals(QUESTIONS_LIST.size(), service.getQuestions().size());

        assertEquals(EXPECTATIONS, service.getQuestions());
    }

    @Test
    void getAnswers() {
        QuestionService service = new QuestionServiceImpl(dataStorage, answersStorage);

        assertEquals(QUESTIONS_LIST.size(), service.getAnswers().length);

        assertEquals(ANSWERS, service.getAnswers());
    }
}