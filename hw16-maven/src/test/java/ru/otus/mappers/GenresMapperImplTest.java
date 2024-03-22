package ru.otus.mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.requests.GenresRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

@DisplayName("Mapper для работы с жанрами должен")
@SpringBootTest
class GenresMapperImplTest {

    private final static Long TEST_GENRE_ID = 1L;
    private final static String TEST_GENRE_NAME = "GENRE";
    private final static String TEST_BOOK_NAME = "name";
    private final static Book TEST_BOOK = new Book(
            null,
            TEST_BOOK_NAME,
            List.of(),
            List.of(),
            List.of());
    private final static Genre TEST_GENRE = new Genre(TEST_GENRE_ID, TEST_GENRE_NAME, List.of(TEST_BOOK));
    private final static GenresRequest TEST_GENRES_REQUEST = new GenresRequest(TEST_GENRE_NAME);

    @MockBean
    private BookRequestMapper bookMapper;

    @Autowired
    private GenresMapper mapper;

    @Test
    @DisplayName("создавать Genre на основе GenresRequest")
    void create() {

        var result = mapper.create(TEST_GENRES_REQUEST);

        assertEquals(TEST_GENRE_NAME, result.getName());
        assertNotNull(result.getBooks());
        assertTrue(result.getBooks().isEmpty());
    }

    @Test
    @DisplayName("обновлять Genre на основе GenresRequest")
    void update() {
        var result = new Genre();

        mapper.update(result, TEST_GENRES_REQUEST);

        assertEquals(TEST_GENRE_NAME, result.getName());
        assertNotNull(result.getBooks());
        assertTrue(result.getBooks().isEmpty());
    }

    @Test
    @DisplayName("создавать GenresResponse на основе Genre")
    void toDto() {
        doAnswer(invocationOnMock -> {
            Book book = invocationOnMock.getArgument(0);
            return new BooksResponse(null, book.getName(), null, null, null);
        }).when(bookMapper).toDto(eq(TEST_BOOK));

        var result = mapper.toDto(TEST_GENRE);

        assertEquals(TEST_GENRE.getId(), result.getId());
        assertEquals(TEST_GENRE.getName(), result.getName());
        assertEquals(TEST_GENRE.getBooks().size(), result.getBooks().size());
        assertTrue(result.getBooks().stream().allMatch(book -> TEST_BOOK_NAME.equals(book.getName())));
    }
}