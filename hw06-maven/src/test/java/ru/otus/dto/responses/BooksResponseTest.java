package ru.otus.dto.responses;

import org.junit.jupiter.api.Test;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Book;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BooksResponseTest {

    @Test
    void testToString() {
        var result = new BooksResponse(
                new Book(
                        null,
                        null,
                        List.of(),
                        List.of()))
                .toString();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
    }
}