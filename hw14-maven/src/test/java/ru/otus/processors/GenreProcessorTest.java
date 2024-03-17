package ru.otus.processors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.postgre.entities.Genre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = {
        GenreProcessor.class
})
class GenreProcessorTest {

    private final static String GENRE_NAME = "GENRE_NAME";


    @Autowired
    private GenreProcessor processor;

    @Test
    @DisplayName("Преобразует JPA Entity в Mongo документ")
    void process() {
        var result = processor.process(new Genre(null, GENRE_NAME));

        assertNotNull(result);
        assertEquals(GENRE_NAME, result.getName());

    }
}