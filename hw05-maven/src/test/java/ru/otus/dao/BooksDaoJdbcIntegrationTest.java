package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.dao.decorators.NamedParameterJdbcJdbcDecorators;
import ru.otus.entities.Authors;
import ru.otus.entities.Books;
import ru.otus.entities.Genres;
import ru.otus.mappers.rows.AuthorsMapperImpl;
import ru.otus.mappers.rows.BooksMapperImpl;
import ru.otus.mappers.rows.GenresMapperImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dao для работы с книгами должно")
@JdbcTest
@Import({
        BooksDaoJdbc.class,
        AuthorsDaoJdbc.class,
        GenresDaoJdbc.class,
        NamedParameterJdbcJdbcDecorators.class,
        BooksMapperImpl.class,
        GenresMapperImpl.class,
        AuthorsMapperImpl.class
})
class BooksDaoJdbcIntegrationTest {
    private static final Integer EXPECTED_BOOKS_COUNT = 1;
    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final Long OTHER_BOOKS_ID = 2L;
    private static final String EXISTING_BOOKS_NAME = "books_name";
    private static final String OTHER_BOOKS_NAME = "books_name_alt";
    private static final String EXISTING_AUTHOR_NAME = "Ivan";
    private static final String OTHER_AUTHOR_NAME = "Petr";
    private static final String EXISTING_GENRES_NAME = "drama";
    private static final String OTHER_GENRES_NAME = "comedy";

    @Autowired
    private BooksDao booksDao;

    @Test
    @DisplayName("возвращать книгу по её id")
    void getById() {
        var result = booksDao.getById(EXISTING_BOOKS_ID);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(EXISTING_BOOKS_ID, result.map(Books::getId).orElse(null));
        assertEquals(EXISTING_BOOKS_NAME, result.map(Books::getName).orElse(null));
        assertEquals(
                EXISTING_AUTHOR_NAME,
                result
                        .map(Books::getAuthors)
                        .map(authors -> authors.get(0))
                        .map(Authors::name)
                        .orElse(null));
        assertEquals(
                EXISTING_GENRES_NAME,
                result
                        .map(Books::getGenres)
                        .map(genres -> genres.get(0))
                        .map(Genres::name)
                        .orElse(null));
    }

    @Test
    @DisplayName("возвращать ожидаемый список книг")
    void getAll() {
        var result = booksDao.getAll();

        assertEquals(EXPECTED_BOOKS_COUNT, result.size());
        assertEquals(EXISTING_AUTHOR_NAME, result.get(0).getAuthors().get(0).name());
        assertEquals(EXISTING_GENRES_NAME, result.get(0).getGenres().get(0).name());
    }

    @Test
    @DisplayName("добавлять книги в БД")
    void create() {
        var result = booksDao.create(
                new Books(
                        null,
                        OTHER_BOOKS_NAME,
                        List.of(new Authors(null, OTHER_AUTHOR_NAME)),
                        List.of(new Genres(null, OTHER_GENRES_NAME))));

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(OTHER_BOOKS_NAME, result.getName());
        assertFalse(result.getAuthors().isEmpty());
        assertEquals(OTHER_AUTHOR_NAME, result.getAuthors().get(0).name());
        assertFalse(result.getGenres().isEmpty());
        assertEquals(OTHER_GENRES_NAME, result.getGenres().get(0).name());

    }

    @Test
    void update() {
        var result = booksDao.create(
                new Books(
                        null,
                        EXISTING_GENRES_NAME,
                        List.of(),
                        List.of()));
        var id = result.getId();

        result = booksDao.update(
                new Books(
                        id,
                        OTHER_BOOKS_NAME,
                        List.of(new Authors(null, OTHER_AUTHOR_NAME)),
                        List.of(new Genres(null, OTHER_GENRES_NAME))));

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(id, result.getId());
        assertEquals(OTHER_BOOKS_NAME, result.getName());
        assertFalse(result.getAuthors().isEmpty());
        assertEquals(OTHER_AUTHOR_NAME, result.getAuthors().get(0).name());
        assertFalse(result.getGenres().isEmpty());
        assertEquals(OTHER_GENRES_NAME, result.getGenres().get(0).name());
    }

    @Test
    @DisplayName("выдовать исключение если id = null")
    void updateForNullId() {
        assertThrows(RuntimeException.class, () -> booksDao.update(new Books(null, null, null, null)));
    }

    @Test
    @DisplayName("удалять книгу по её id")
    void delete() {
        var books = booksDao.create(new Books(null, OTHER_BOOKS_NAME, List.of(), List.of()));

        assertTrue(booksDao.exist(books.getId()));

        booksDao.delete(books.getId());

        assertFalse(booksDao.exist(books.getId()));
    }

    @Test
    @DisplayName("определять что книга с заданым id существует в БД")
    void exist() {
        assertTrue(booksDao.exist(EXISTING_BOOKS_ID));
        assertFalse(booksDao.exist(EXISTING_BOOKS_ID + OTHER_BOOKS_ID));
    }

    @Test
    @DisplayName("возвращать ожидаемое количество книг в БД")
    void count() {
        assertEquals(EXPECTED_BOOKS_COUNT, booksDao.count());
    }

    @Test
    @DisplayName("возвращать список книг по названию")
    void getByName() {
        var result = booksDao.getByName(EXISTING_BOOKS_NAME);

        assertEquals(EXPECTED_BOOKS_COUNT, result.size());
        assertEquals(EXISTING_BOOKS_NAME, result.get(0).getName());
        assertEquals(EXISTING_AUTHOR_NAME, result.get(0).getAuthors().get(0).name());
        assertEquals(EXISTING_GENRES_NAME, result.get(0).getGenres().get(0).name());
    }

    @Test
    @DisplayName("возвращать список книг по имени автора")
    void getByAuthor() {
        var result = booksDao.getByAuthor(EXISTING_AUTHOR_NAME);

        assertEquals(EXPECTED_BOOKS_COUNT, result.size());
        assertEquals(EXISTING_BOOKS_NAME, result.get(0).getName());
        assertEquals(EXISTING_AUTHOR_NAME, result.get(0).getAuthors().get(0).name());
        assertEquals(EXISTING_GENRES_NAME, result.get(0).getGenres().get(0).name());
    }

    @Test
    @DisplayName("возвращать список книг по жаннту")
    void getByGenre() {
        var result = booksDao.getByGenre(EXISTING_GENRES_NAME);

        assertEquals(EXPECTED_BOOKS_COUNT, result.size());
        assertEquals(EXISTING_BOOKS_NAME, result.get(0).getName());
        assertEquals(EXISTING_AUTHOR_NAME, result.get(0).getAuthors().get(0).name());
        assertEquals(EXISTING_GENRES_NAME, result.get(0).getGenres().get(0).name());
    }
}