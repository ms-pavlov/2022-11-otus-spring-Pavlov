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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий на основе Jpa для работы с авторами ")
@DataJpaTest
@Import(AuthorsRepositoryImpl.class)
class AuthorsRepositoryImplIntegrationTest {

    private static final int EXPECTED_QUERIES_COUNT = 1;
    private static final Integer EXPECTED_AUTHOR_COUNT = 1;
    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final Long EXISTING_AUTHOR_ID = 1L;
    private static final Long OTHER_AUTHOR_ID = 2L;
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
        var result = repository.getById(EXISTING_AUTHOR_ID);

        assertTrue(result.isPresent());
        assertEquals(EXISTING_AUTHOR_NAME, result.map(Author::getName).orElse(null));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    @DisplayName("возвращать ожидаемый список авторов")
    void getAll() {

        var result = repository.getAll();

        assertEquals(EXPECTED_AUTHOR_COUNT, result.size());
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    @DisplayName("добавлять авторов в БД")
    void create() {
        var author = new Author(null, OTHER_AUTHOR_NAME);
        author.setBooks(List.of(new Book(null, BOOK_NAME, List.of(author), null)));

        var result = repository.create(author);

        assertEquals(OTHER_AUTHOR_NAME, result.getName());
        assertNotNull(result.getId());
        assertEquals(BOOK_NAME, result.getBooks().get(0).getName());

        repository.delete(result.getId());
    }

    @Test
    @DisplayName("обновлять авторов в БД")
    void update() {
        var author = repository.getById(EXISTING_AUTHOR_ID)
                .map(value -> {
                    value.setName(OTHER_AUTHOR_NAME);
                    value.getBooks().clear();
                    value.getBooks().add(new Book(null, BOOK_NAME, List.of(value), List.of()));
                    return value;
                })
                .orElse(null);

        var result = repository.update(author);

        assertEquals(OTHER_AUTHOR_NAME, result.getName());
        assertNotNull(result.getId());
        assertEquals(BOOK_NAME, result.getBooks().get(0).getName());
    }

    @Test
    @DisplayName("удалять заданного автора по его id")
    void delete() {

        var authors = repository.create(new Author(null, OTHER_AUTHOR_NAME));

        assertTrue(repository.exist(authors.getId()));

        repository.delete(authors.getId());

        assertFalse(repository.exist(authors.getId()));
        assertFalse(repository.getById(authors.getId()).isPresent());
    }

    @Test
    @DisplayName("определять что автор с заданым id существует в БД")
    void exist() {

        assertTrue(repository.exist(EXISTING_AUTHOR_ID));
        assertFalse(repository.exist(EXISTING_AUTHOR_ID + OTHER_AUTHOR_ID));

    }

    @Test
    @DisplayName("возвращать ожидаемое количество авторов в БД")
    void count() {
        assertEquals(EXPECTED_AUTHOR_COUNT, repository.count());
    }

    @Test
    @DisplayName("возвращать список авторов по имени")
    void findByName() {
        var result = repository.getByName(EXISTING_AUTHOR_NAME);

        assertNotNull(result);
        assertEquals(EXISTING_AUTHOR_ID, result.getId());
        assertEquals(EXISTING_AUTHOR_NAME, result.getName());
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @Test
    @DisplayName("возвращать список авторов по имени")
    void findByNameNotExist() {
        var result = repository.getByName(OTHER_AUTHOR_NAME);

        assertNull(result);
    }
}