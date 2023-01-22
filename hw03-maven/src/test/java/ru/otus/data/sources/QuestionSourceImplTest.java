package ru.otus.data.sources;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.otus.config.AppPropertiesConfig;
import ru.otus.config.PropertiesConfig;
import ru.otus.data.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class QuestionSourceImplTest {
    private final static List<Question> EXPECTATION_LIST = List.of(
            new Question("2 x 2 = ?", List.of("3", "5"), "4"),
            new Question("7 x 7 = ?", List.of("47", "77"), "49"),
            new Question("8 x 8 = ?", List.of("68", "88"), "64"),
            new Question("4 x 4 = ?", List.of("14", "44"), "16"),
            new Question("11 x 11 = ?", List.of("111", "1111"), "121")
    );

    @SpyBean
    PropertiesConfig config;

    @Test
    public void testFindAll() {
        when(config.getPath()).thenReturn("./test.csv");
        QuestionDataSource dataSource = new QuestionSourceImpl(config);

        var result = dataSource.getQuestions();

        assertTrue(EXPECTATION_LIST.containsAll(result));
        assertTrue(result.containsAll(EXPECTATION_LIST));
    }

    @Test
    public void testFileNotFound() {
        when(config.getPath()).thenReturn("/testFileNotFound.csv");
        QuestionDataSource dataSource = new QuestionSourceImpl(config);
        assertThrows(RuntimeException.class, dataSource::getQuestions);
    }

    @Test
    public void testEmptyFilePath() {
        when(config.getPath()).thenReturn("");
        QuestionDataSource dataSource = new QuestionSourceImpl(config);
        assertThrows(RuntimeException.class, dataSource::getQuestions);
    }

}