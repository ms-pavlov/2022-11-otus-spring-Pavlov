package ru.otus.mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;
import ru.otus.repositories.AuthorsRepository;
import ru.otus.repositories.GenresRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Mapper для работы с книгами должен")
@SpringBootTest(classes = {
        AuthorsRepository.class,
        GenresRepository.class,
        BookRequestMapperImpl.class
})
class BookRequestMapperImplTest {
    private final static String TEST_BOOK_NAME = "name";
    private final static String TEST_AUTHORS_NAME = "Authors";
    private final static String TEST_GENRES_NAME = "Genres";
    private final static Author TEST_AUTHORS = new Author(null, TEST_AUTHORS_NAME);
    private final static Genre TEST_GENRES = new Genre(null, TEST_GENRES_NAME);
    private final static Book TEST_BOOK = new Book(
            null,
            TEST_BOOK_NAME,
            List.of(TEST_AUTHORS),
            List.of(TEST_GENRES));
    private final static BooksRequest TEST_REQUEST = new BooksRequest(
            TEST_BOOK_NAME,
            List.of(TEST_AUTHORS_NAME),
            List.of(TEST_GENRES_NAME));


    @MockBean
    private AuthorsRepository authorsRepository;
    @MockBean
    private GenresRepository genresRepository;

    @Autowired
    private BookRequestMapper mapper;

    @Test
    @DisplayName("создавать Books на основе BooksRequest")
    void create() {
        when(authorsRepository.getByName(TEST_AUTHORS_NAME)).thenReturn(TEST_AUTHORS);
        when(genresRepository.getByName(TEST_GENRES_NAME)).thenReturn(TEST_GENRES);

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
        when(authorsRepository.getByName(TEST_AUTHORS_NAME)).thenReturn(null);
        when(genresRepository.getByName(TEST_GENRES_NAME)).thenReturn(null);
        when(authorsRepository.create(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(genresRepository.create(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        var result = new Book(null, null, null, null);

        mapper.update(result, TEST_REQUEST);

        assertEquals(TEST_BOOK_NAME, result.getName());
        assertEquals(TEST_REQUEST.authors().size(), result.getAuthors().size());
        assertTrue(result.getAuthors().stream().allMatch(authors -> TEST_AUTHORS_NAME.equals(authors.getName())));
        assertEquals(TEST_REQUEST.genres().size(), result.getGenres().size());
        assertTrue(result.getGenres().stream().allMatch(genres -> TEST_GENRES_NAME.equals(genres.getName())));
    }

    @Test
    @DisplayName("обновлять Books на основе BooksRequest")
    void update() {
        when(authorsRepository.getByName(TEST_AUTHORS_NAME)).thenReturn(TEST_AUTHORS);
        when(genresRepository.getByName(TEST_GENRES_NAME)).thenReturn(TEST_GENRES);

        var result = new Book(null, null, null, null);

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