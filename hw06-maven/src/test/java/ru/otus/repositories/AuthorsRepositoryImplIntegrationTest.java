package ru.otus.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.entities.Author;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий на основе Jpa для работы с авторами ")
@DataJpaTest
@Import(AuthorsRepositoryImpl.class)
class AuthorsRepositoryImplIntegrationTest {

    private static final Integer EXPECTED_AUTHOR_COUNT = 1;
    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final Long EXISTING_AUTHOR_ID = 1L;
    private static final Long OTHER_AUTHOR_ID = 2L;
    private static final String EXISTING_AUTHOR_NAME = "Ivan";
    private static final String OTHER_AUTHOR_NAME = "Petr";

    @Autowired
    private AuthorsRepository repository;

    @Test
    @DisplayName("возвращать автора по его id")
    void getById() {
        var result = repository.getById(EXISTING_AUTHOR_ID);

        assertTrue(result.isPresent());
        assertEquals(EXISTING_AUTHOR_NAME, result.map(Author::getName).orElse(null));
    }

    @Test
    @DisplayName("возвращать ожидаемый список авторов")
    void getAll() {
        var result = repository.getAll();

        assertEquals(EXPECTED_AUTHOR_COUNT, result.size());
    }

    @Test
    @DisplayName("добавлять авторов в БД")
    void create() {
        var result = repository.create(new Author(null, OTHER_AUTHOR_NAME, List.of()));

        assertEquals(OTHER_AUTHOR_NAME, result.getName());
        assertNotNull(result.getId());

        repository.delete(result.getId());
    }

    @Test
    @DisplayName("обновлять авторов в БД")
    void update() {
        var result = repository.update(new Author(EXISTING_AUTHOR_ID, OTHER_AUTHOR_NAME, List.of()));

        assertEquals(OTHER_AUTHOR_NAME, result.getName());
        assertNotNull(result.getId());
    }

    @Test
    @DisplayName("удалять заданного автора по его id")
    void delete() {
        var authors = repository.create(new Author(null, OTHER_AUTHOR_NAME, List.of()));

        assertTrue(repository.exist(authors.getId()));

        repository.delete(authors.getId());

        assertFalse(repository.exist(authors.getId()));
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
    @DisplayName("возвращать список авторов по id книги")
    void findByBookId() {
        var result = repository.findByBookId(EXISTING_BOOKS_ID);

        assertNotNull(result);
        assertEquals(EXPECTED_AUTHOR_COUNT, result.size());
    }

    @Test
    @DisplayName("возвращать список авторов по имени")
    void findByName() {
        var result = repository.findByName(EXISTING_AUTHOR_NAME);

        assertNotNull(result);
        assertEquals(EXISTING_AUTHOR_ID, result.getId());
        assertEquals(EXISTING_AUTHOR_NAME, result.getName());
    }

    @Test
    @DisplayName("возвращать список авторов по имени")
    void findByNameNotExist() {
        var result = repository.findByName(OTHER_AUTHOR_NAME);

        assertNull(result);
    }
}