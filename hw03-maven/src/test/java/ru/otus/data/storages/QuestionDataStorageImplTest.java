package ru.otus.data.storages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.data.Question;
import ru.otus.data.sources.QuestionDataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class QuestionDataStorageImplTest {

    private final static List<Question> QUESTIONS_LIST = List.of(
            new Question("2 x 2 = ?", List.of("=3", "=5"), "=4"),
            new Question("7 x 7 = ?", List.of("47", "=77"), "=49"),
            new Question("8 x 8 = ?", List.of("68", "=88"), "=64"),
            new Question("4 x 4 = ?", List.of("14", "=44"), "=16"),
            new Question("11 x 11 = ?", List.of("111", "=1111"), "=121")
    );

    @MockBean
    private QuestionDataSource dataSource;

    @BeforeEach
    void setUp() {
        when(dataSource.getQuestions()).thenReturn(QUESTIONS_LIST);
    }

    @Test
    void find() {
        var storage = new QuestionDataStorageImpl(dataSource);
        Question question = storage.find(0);

        assertEquals(QUESTIONS_LIST.get(0), question);

        question.setQuestionText("error" + question.getQuestionText());

        assertNotEquals(QUESTIONS_LIST.get(0), question);
    }

    @Test
    void getCount() {
        QuestionDataStorage storage = new QuestionDataStorageImpl(dataSource);

        assertEquals(QUESTIONS_LIST.size(), storage.getCount());
    }

    @Test
    void findAll() {
        QuestionDataStorage storage = new QuestionDataStorageImpl(dataSource);

        var result = storage.findAll();

        assertTrue(QUESTIONS_LIST.containsAll(result));
        assertTrue(result.containsAll(QUESTIONS_LIST));

        result.get(0).setQuestionText("error" + QUESTIONS_LIST.get(0).getQuestionText());

        assertFalse(QUESTIONS_LIST.containsAll(result));
        assertFalse(result.containsAll(QUESTIONS_LIST));
    }

    @Test
    void findAllNull() {
        QuestionDataStorage storage = new QuestionDataStorageImpl(dataSource);
        when(dataSource.getQuestions()).thenReturn(null);
        var result = storage.findAll();

        assertNull(result);
    }

    @Test
    void getTemplate() {
        QuestionDataStorage storage = new QuestionDataStorageImpl(dataSource);

        assertEquals(QUESTIONS_LIST.stream().map(question -> question.getQuestionDecisions().size()).toList(),
                storage.getTemplate());
    }
}