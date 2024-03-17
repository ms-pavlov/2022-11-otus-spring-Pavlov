package ru.otus.processors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.postgre.entities.Author;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = {
        AuthorProcessor.class
})
class AuthorProcessorTest {

    private final static String AUTHOR_NAME = "AUTHOR_NAME";


    @Autowired
    private AuthorProcessor processor;

    @Test
    @DisplayName("Преобразует JPA Entity в Mongo документ")
    void process() {
        var result = processor.process(new Author(null, AUTHOR_NAME));

        assertNotNull(result);
        assertEquals(AUTHOR_NAME, result.getName());

    }
}