package ru.otus.data.storages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.data.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnswersStorageImplTest {
    private final static List<Question> QUESTIONS_LIST = List.of(
            new Question("2 x 2 = ?", List.of("=3", "=5"), "=4"),
            new Question("7 x 7 = ?", List.of("47", "=77"), "=49"),
            new Question("8 x 8 = ?", List.of("68", "=88"), "=64"),
            new Question("4 x 4 = ?", List.of("14", "=44"), "=16"),
            new Question("11 x 11 = ?", List.of("111", "=1111"), "=121")
    );

    private QuestionDataStorage dataStorage;

    @BeforeEach
    void setUp() {
        dataStorage = mock(QuestionDataStorage.class);
        when(dataStorage.getTemplate()).thenReturn(QUESTIONS_LIST.stream()
                .map(question -> question.getQuestionDecisions().size())
                .toList());
    }

    @Test
    void getAnswers() {
        AnswersStorage storage = new AnswersStorageImpl(dataStorage);

        assertEquals(QUESTIONS_LIST.size(), storage.getAnswers().size());

        for (int i = 0; i < QUESTIONS_LIST.size(); i++) {
            assertTrue(QUESTIONS_LIST.get(i).getQuestionDecisions().size() >= storage.getAnswers().get(i));
        }
    }
}