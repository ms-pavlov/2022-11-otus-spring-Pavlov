package ru.otus.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import(GenresRepositoryImpl.class)
class GenresRepositoryImplIntegrationTest {

    private static final int EXPECTED_QUERIES_COUNT = 2;
    private static final Integer EXPECTED_GENRES_COUNT = 2;
    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final Long EXISTING_GENRES_ID = 1L;
    private static final Long OTHER_GENRES_ID = 200L;
    private static final String EXISTING_GENRES_NAME = "drama";
    private static final String OTHER_GENRES_NAME = "comedy";
    private static final String BOOK_NAME = "test";

    @Autowired
    private GenresRepository repository;

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
    @DisplayName("возвращать жанр по имени")
    void getByName() {
        var result = repository.getByName(EXISTING_GENRES_NAME);

        assertNotNull(result);
        assertEquals(EXISTING_GENRES_ID, result.getId());
        assertEquals(EXISTING_GENRES_NAME, result.getName());
        assertTrue( result.getBooks()
                .stream()
                .anyMatch(book -> Objects.equals(book.getId(), EXISTING_BOOKS_ID)));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    @DisplayName("возвращать жанр по его id")
    void getById() {
        var result = repository.getById(EXISTING_GENRES_ID).orElse(null);

        assertNotNull(result);
        assertEquals(EXISTING_GENRES_ID, result.getId());
        assertTrue( result.getBooks()
                                .stream()
                                .anyMatch(book -> Objects.equals(book.getId(), EXISTING_BOOKS_ID)));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    @DisplayName("возвращать ожидаемый список жанров")
    void getAll() {
        var result = repository.getAll();

        assertEquals(EXPECTED_GENRES_COUNT, result.size());
        assertTrue( result.stream().anyMatch(genre -> Objects.equals(genre.getName(), EXISTING_GENRES_NAME)));
        assertTrue( result.stream()
                .anyMatch(
                        genre -> genre.getBooks()
                                .stream()
                                .anyMatch(book -> Objects.equals(book.getId(), EXISTING_BOOKS_ID))));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    @DisplayName("добавлять жанры в БД")
    void create() {
        var genre = new Genre(null, OTHER_GENRES_NAME);
        genre.setBooks(List.of(new Book(null, BOOK_NAME, null, List.of(genre))));

        var result = repository.create(genre);

        assertEquals(OTHER_GENRES_NAME, result.getName());
        assertNotNull(result.getId());
        assertTrue( result.getBooks()
                                .stream()
                                .anyMatch(book -> Objects.equals(book.getName(), BOOK_NAME)));
        assertTrue( result.getBooks()
                .stream()
                .noneMatch(book -> book.getId() == null));

        repository.delete(result.getId());
    }

    @Test
    @DisplayName("Обновляет жанр в БД")
    void update() {
        var genre = repository.getById(EXISTING_GENRES_ID)
                .map(value -> {
                    value.setName(OTHER_GENRES_NAME);
                    value.getBooks().clear();
                    value.getBooks().add(new Book(null, BOOK_NAME, List.of(), List.of(value)));
                    return value;
                })
                .orElse(null);

        var result = repository.update(genre);

        assertEquals(OTHER_GENRES_NAME, result.getName());
        assertNotNull(result.getId());
        assertEquals(BOOK_NAME, result.getBooks().get(0).getName());
    }

    @Test
    @DisplayName("удалять заданного жанр по его id")
    void delete() {
        var genre = repository.create(new Genre(null, OTHER_GENRES_NAME));

        assertTrue(repository.exist(genre.getId()));

        repository.delete(genre.getId());

        assertFalse(repository.exist(genre.getId()));
        assertFalse(repository.getById(genre.getId()).isPresent());
    }

    @Test
    @DisplayName("определять что жанр с заданым id существует в БД")
    void exist() {

        assertTrue(repository.exist(EXISTING_GENRES_ID));
        assertFalse(repository.exist(EXISTING_GENRES_ID + OTHER_GENRES_ID));

    }

    @Test
    @DisplayName("возвращать ожидаемое количество жанров в БД")
    void count() {
        assertEquals(EXPECTED_GENRES_COUNT, repository.count());
    }

    @Test
    @DisplayName("возвращать автора по имени")
    void findByName() {
        var result = repository.getByName(EXISTING_GENRES_NAME);

        assertEquals(EXISTING_GENRES_NAME, result.getName() );
        assertTrue(
                result.getBooks()
                        .stream()
                        .anyMatch(book -> Objects.equals(book.getId(), EXISTING_BOOKS_ID)));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    @DisplayName("возвращать список авторов по имени")
    void findByNameNotExist() {
        var result = repository.getByName(OTHER_GENRES_NAME);

        assertNull(result);
    }
}