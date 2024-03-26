package ru.otus.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.entities.Author;
import ru.otus.entities.Book;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий на основе Jpa для работы с авторами ")
@DataJpaTest
class AuthorsRepositoryImplIntegrationTest {

    private static final int EXPECTED_QUERIES_COUNT = 2;
    private static final Long EXPECTED_AUTHOR_COUNT = 2L;
    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final Long EXISTING_AUTHOR_ID = 1L;
    private static final String EXISTING_AUTHOR_NAME = "Ivan";
    private static final String OTHER_AUTHOR_NAME = "Petr";
    private static final String BOOK_NAME = "test";

    @Autowired
    private AuthorsRepository repository;

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
    @DisplayName("возвращать автора по его id")
    void getById() {
        var result = repository.findById(EXISTING_AUTHOR_ID);

        assertTrue(result.isPresent());
        assertTrue(result.stream().anyMatch(author -> Objects.equals(author.getName(), EXISTING_AUTHOR_NAME)));
        assertTrue(result.stream()
                .anyMatch(
                        author -> author.getBooks()
                                .stream()
                                .anyMatch(book -> Objects.equals(book.getId(), EXISTING_BOOKS_ID))));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    @DisplayName("возвращать ожидаемый список авторов")
    void getAll() {

        var result = repository.findAll();

        assertEquals(EXPECTED_AUTHOR_COUNT, result.size());
        assertTrue(result.stream().anyMatch(author -> Objects.equals(author.getName(), EXISTING_AUTHOR_NAME)));
        assertTrue(result.stream()
                .anyMatch(
                        author -> author.getBooks()
                                .stream()
                                .anyMatch(book -> Objects.equals(book.getId(), EXISTING_BOOKS_ID))));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    @DisplayName("добавлять авторов в БД")
    void create() {
        var author = new Author(null, OTHER_AUTHOR_NAME);
        author.setBooks(List.of(new Book(null, BOOK_NAME, List.of(author), null, null)));

        var result = repository.save(author);

        assertEquals(OTHER_AUTHOR_NAME, result.getName());
        assertNotNull(result.getId());
        assertEquals(BOOK_NAME, result.getBooks().get(0).getName());

        repository.deleteById(result.getId());
    }

    @Test
    @DisplayName("обновлять авторов в БД")
    void update() {
        var author = repository.findById(EXISTING_AUTHOR_ID)
                .map(value -> {
                    value.setName(OTHER_AUTHOR_NAME);
                    value.getBooks().clear();
                    value.getBooks().add(new Book(null, BOOK_NAME, List.of(value), List.of(), List.of()));
                    return value;
                })
                .orElse(null);

        var result = repository.save(author);

        assertEquals(OTHER_AUTHOR_NAME, result.getName());
        assertNotNull(result.getId());
    }

    @Test
    @DisplayName("возвращать ожидаемое количество авторов в БД")
    void count() {
        assertEquals(EXPECTED_AUTHOR_COUNT, repository.count());
    }

    @Test
    @DisplayName("возвращать автора по имени")
    void findByName() {
        var result = repository.findByName(EXISTING_AUTHOR_NAME);

        assertEquals(EXISTING_AUTHOR_NAME, result.getName());
        assertTrue(
                result.getBooks()
                        .stream()
                        .anyMatch(book -> Objects.equals(book.getId(), EXISTING_BOOKS_ID)));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    @DisplayName("возвращать список авторов по имени")
    void findByNameNotExist() {
        var result = repository.findByName(OTHER_AUTHOR_NAME);

        assertNull(result);
    }

}