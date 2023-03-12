package ru.otus.mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dao.AuthorsDao;
import ru.otus.dao.GenresDao;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.entities.Authors;
import ru.otus.entities.Books;
import ru.otus.entities.Genres;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Mapper для работы с книгами должен")
@SpringBootTest
class BookRequestMapperImplTest {
    private final static String TEST_BOOK_NAME = "name";
    private final static String TEST_AUTHORS_NAME = "Authors";
    private final static String TEST_GENRES_NAME = "Genres";
    private final static Authors TEST_AUTHORS = new Authors(null, TEST_AUTHORS_NAME);
    private final static Genres TEST_GENRES = new Genres(null, TEST_GENRES_NAME);
    private final static Books TEST_BOOK = new Books(
            null,
            TEST_BOOK_NAME,
            List.of(TEST_AUTHORS),
            List.of(TEST_GENRES));
    private final static BooksRequest TEST_REQUEST = new BooksRequest(
            TEST_BOOK_NAME,
            List.of(TEST_AUTHORS_NAME),
            List.of(TEST_GENRES_NAME));


    @MockBean
    private AuthorsDao authorsDao;
    @MockBean
    private GenresDao genresDao;

    @Autowired
    private BookRequestMapper mapper;

    @Test
    @DisplayName("создавать Books на основе BooksRequest")
    void create() {
        when(authorsDao.findByName(TEST_AUTHORS_NAME)).thenReturn(TEST_AUTHORS);
        when(genresDao.findByName(TEST_GENRES_NAME)).thenReturn(TEST_GENRES);

        var result = mapper.create(TEST_REQUEST);

        assertEquals(TEST_BOOK_NAME, result.getName());
        assertEquals(TEST_REQUEST.authors().size(), result.getAuthors().size());
        assertTrue(result.getAuthors().contains(TEST_AUTHORS));
        assertEquals(TEST_REQUEST.genres().size(), result.getGenres().size());
        assertTrue(result.getGenres().contains(TEST_GENRES));
    }

    @Test
    @DisplayName("обновлять Books на основе BooksRequest и создавать авторов и жанры при необходимости")
    void updateAndCreateAuthorsAndGenres() {
        when(authorsDao.findByName(TEST_AUTHORS_NAME)).thenReturn(null);
        when(genresDao.findByName(TEST_GENRES_NAME)).thenReturn(null);
        when(authorsDao.create(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(genresDao.create(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        var result = new Books(null, null, null, null);

        mapper.update(result, TEST_REQUEST);

        assertEquals(TEST_BOOK_NAME, result.getName());
        assertEquals(TEST_REQUEST.authors().size(), result.getAuthors().size());
        assertTrue(result.getAuthors().stream().allMatch(authors -> authors.name().equals(TEST_AUTHORS_NAME)));
        assertEquals(TEST_REQUEST.genres().size(), result.getGenres().size());
        assertTrue(result.getGenres().stream().allMatch(genres -> genres.name().equals(TEST_GENRES_NAME)));
    }

    @Test
    @DisplayName("обновлять Books на основе BooksRequest")
    void update() {
        when(authorsDao.findByName(TEST_AUTHORS_NAME)).thenReturn(TEST_AUTHORS);
        when(genresDao.findByName(TEST_GENRES_NAME)).thenReturn(TEST_GENRES);

        var result = new Books(null, null, null, null);

        mapper.update(result, TEST_REQUEST);

        assertEquals(TEST_BOOK_NAME, result.getName());
        assertEquals(TEST_REQUEST.authors().size(), result.getAuthors().size());
        assertTrue(result.getAuthors().contains(TEST_AUTHORS));
        assertEquals(TEST_REQUEST.genres().size(), result.getGenres().size());
        assertTrue(result.getGenres().contains(TEST_GENRES));
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