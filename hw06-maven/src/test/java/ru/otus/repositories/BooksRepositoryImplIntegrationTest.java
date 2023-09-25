package ru.otus.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import(BooksRepositoryImpl.class)
class BooksRepositoryImplIntegrationTest {

    private static final int EXPECTED_QUERIES_COUNT = 3;
    private static final Long EXPECTED_BOOKS_COUNT = 3L;
    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final String EXISTING_BOOKS_NAME = "books_name";
    private static final String OTHER_BOOKS_NAME = "books_name_alt";
    private static final String EXISTING_AUTHOR_NAME = "Ivan";
    private static final String OTHER_AUTHOR_NAME = "Petr";
    private static final String EXISTING_GENRES_NAME = "drama";
    private static final String OTHER_GENRES_NAME = "comedy";

    @Autowired
    private BooksRepository repository;

    @Autowired
    private TestEntityManager em;

    private SessionFactory sessionFactory;

    @BeforeEach
    void setUp() {
        sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
    }

    @Test
    @DisplayName("возвращать книгу по названию")
    void getByName() {
        var result = repository.getByName(EXISTING_BOOKS_NAME);

        assertEquals(1, result.size());
        assertTrue(
                result.stream()
                        .anyMatch(
                                book -> Objects.equals(book.getName(), EXISTING_BOOKS_NAME)));
        assertEquals(
                2,
                result.stream()
                        .map(Book::getAuthors)
                        .mapToLong(Collection::size)
                        .sum());
        assertEquals(
                2,
                result.stream()
                        .map(Book::getGenres)
                        .mapToLong(Collection::size)
                        .sum());
        assertEquals(
                1,
                result.stream()
                        .map(Book::getComments)
                        .mapToLong(Collection::size)
                        .sum());
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("возвращать книгу по её id")
    void getById() {
        var result = repository.getById(EXISTING_BOOKS_ID);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(EXISTING_BOOKS_ID, result.map(Book::getId).orElse(null));
        assertEquals(EXISTING_BOOKS_NAME, result.map(Book::getName).orElse(null));
        assertEquals(
                EXISTING_AUTHOR_NAME,
                result
                        .map(Book::getAuthors)
                        .map(authors -> authors.get(0))
                        .map(Author::getName)
                        .orElse(null));
        assertEquals(
                EXISTING_GENRES_NAME,
                result
                        .map(Book::getGenres)
                        .map(genres -> genres.get(0))
                        .map(Genre::getName)
                        .orElse(null));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    @DisplayName("возвращать ожидаемый список книг")
    void getAll() {
        var result = repository.getAll();

        assertEquals(EXPECTED_BOOKS_COUNT, result.size());
        assertEquals(
                6,
                result.stream()
                        .map(Book::getAuthors)
                        .mapToLong(Collection::size)
                        .sum());
        assertEquals(
                6,
                result.stream()
                        .map(Book::getGenres)
                        .mapToLong(Collection::size)
                        .sum());
        assertEquals(
                4,
                result.stream()
                        .map(Book::getComments)
                        .mapToLong(Collection::size)
                        .sum());
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("добавлять книги в БД")
    void create() {
        var book = new Book(
                null,
                OTHER_BOOKS_NAME,
                null,
                List.of(new Genre(null, OTHER_GENRES_NAME)),
                null);
        book.setAuthors(List.of(new Author(null, OTHER_AUTHOR_NAME)));
        var result = repository.create(book);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(OTHER_BOOKS_NAME, result.getName());
        assertFalse(result.getAuthors().isEmpty());
        assertNotNull(result.getAuthors().get(0).getId());
        assertEquals(OTHER_AUTHOR_NAME, result.getAuthors().get(0).getName());
        assertNotNull(result.getGenres().get(0).getId());
        assertEquals(OTHER_GENRES_NAME, result.getGenres().get(0).getName());
    }

    @Test
    @DisplayName("Обновляет книги в БД")
    void update() {
        var book = repository.getById(EXISTING_BOOKS_ID)
                .orElse(null);
        var author = new Author(null, OTHER_AUTHOR_NAME);
        var genre = new Genre(null, OTHER_GENRES_NAME);
        assert book != null;
        book.setName(OTHER_BOOKS_NAME);
        book.getAuthors().clear();
        book.getAuthors().add(author);
        book.getGenres().clear();
        book.getGenres().add(genre);

        book = repository.update(book);
        var result = repository.getById(book.getId()).orElse(null);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(OTHER_BOOKS_NAME, result.getName());
        assertFalse(result.getAuthors().isEmpty());
        assertEquals(1, result.getAuthors().size());
        assertEquals(OTHER_AUTHOR_NAME, result.getAuthors().get(0).getName());
        assertFalse(result.getGenres().isEmpty());
        assertEquals(1, result.getGenres().size());
        assertEquals(OTHER_GENRES_NAME, result.getGenres().get(0).getName());
    }

    @Test
    @DisplayName("возвращать ожидаемое количество книг в БД")
    void count() {
        assertEquals(EXPECTED_BOOKS_COUNT, repository.count());
    }
}