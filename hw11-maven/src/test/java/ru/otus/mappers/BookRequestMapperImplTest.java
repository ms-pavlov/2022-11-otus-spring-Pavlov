package ru.otus.mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.entities.Book;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Mapper для работы с книгами должен")
@SpringBootTest(classes = {
        BookRequestMapperImpl.class
})
class BookRequestMapperImplTest {
    private final static String TEST_BOOK_NAME = "name";
    private final static String TEST_AUTHORS_NAME = "Authors";
    private final static String TEST_GENRES_NAME = "Genres";
    private final static Book TEST_BOOK = new Book(
            null,
            TEST_BOOK_NAME,
            List.of(),
            List.of());
    private final static BooksRequest TEST_REQUEST = new BooksRequest(
            TEST_BOOK_NAME,
            List.of(TEST_AUTHORS_NAME),
            List.of(TEST_GENRES_NAME));

    @Autowired
    private BookRequestMapper mapper;

    @Test
    @DisplayName("создавать Books на основе BooksRequest")
    void create() {

        var result = mapper.create(TEST_REQUEST);

        assertEquals(TEST_BOOK_NAME, result.getName());
        assertEquals(TEST_REQUEST.getAuthors().size(), result.getAuthors().size());
        assertTrue(result.getAuthors().contains(TEST_AUTHORS_NAME));
        assertEquals(TEST_REQUEST.getGenres().size(), result.getGenres().size());
        assertTrue(result.getGenres().contains(TEST_GENRES_NAME));
    }

    @Test
    @DisplayName("обновлять Books на основе BooksRequest")
    void update() {

        var result = new Book(null, null, new ArrayList<>(), new ArrayList<>());

        mapper.update(result, TEST_REQUEST);

        assertEquals(TEST_BOOK_NAME, result.getName());
        assertEquals(TEST_REQUEST.getAuthors().size(), result.getAuthors().size());
        assertTrue(result.getAuthors().contains(TEST_AUTHORS_NAME));
        assertEquals(TEST_REQUEST.getGenres().size(), result.getGenres().size());
        assertTrue(result.getGenres().contains(TEST_GENRES_NAME));
    }

    @Test
    @DisplayName("создавать BooksResponse на основе Books")
    void toDto() {
        var result = mapper.toDto(TEST_BOOK);

        assertEquals(TEST_BOOK.getId(), result.getId());
        assertEquals(TEST_BOOK.getName(), result.getName());
        assertEquals(TEST_BOOK.getAuthors().size(), result.getAuthors().size());
        assertTrue(result.getAuthors().stream().allMatch(authors -> authors.equals(TEST_AUTHORS_NAME)));
        assertEquals(TEST_BOOK.getGenres().size(), result.getGenres().size());
        assertTrue(result.getGenres().stream().allMatch(genres -> genres.equals(TEST_GENRES_NAME)));
    }
}