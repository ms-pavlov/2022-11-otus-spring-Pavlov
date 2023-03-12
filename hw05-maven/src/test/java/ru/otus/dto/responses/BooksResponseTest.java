package ru.otus.dto.responses;

import org.junit.jupiter.api.Test;
import ru.otus.entities.Books;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BooksResponseTest {

    @Test
    void testToString() {
        var result = new BooksResponse(
                new Books(
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