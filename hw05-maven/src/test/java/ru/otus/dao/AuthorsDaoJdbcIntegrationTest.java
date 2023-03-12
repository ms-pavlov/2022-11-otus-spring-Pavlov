package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.dao.decorators.NamedParameterJdbcJdbcDecorators;
import ru.otus.entities.Authors;
import ru.otus.mappers.rows.AuthorsMapperImpl;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dao для работы с авторами должно")
@JdbcTest
@Import({AuthorsDaoJdbc.class, NamedParameterJdbcJdbcDecorators.class, AuthorsMapperImpl.class})
class AuthorsDaoJdbcIntegrationTest {
    private static final Integer EXPECTED_AUTHOR_COUNT = 1;
    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final Long EXISTING_AUTHOR_ID = 1L;
    private static final Long OTHER_AUTHOR_ID = 2L;
    private static final String EXISTING_AUTHOR_NAME = "Ivan";
    private static final String OTHER_AUTHOR_NAME = "Petr";

    @Autowired
    private AuthorsDao authorsDao;

    @Test
    @DisplayName("возвращать автора по его id")
    void getById() {
        var result = authorsDao.getById(EXISTING_AUTHOR_ID);

        assertTrue(result.isPresent());
        assertEquals(EXISTING_AUTHOR_NAME, result.map(Authors::name).orElse(null));
    }

    @Test
    @DisplayName("возвращать ожидаемый список авторов")
    void getAll() {
        var result = authorsDao.getAll();

        assertEquals(EXPECTED_AUTHOR_COUNT, result.size());
    }

    @Test
    @DisplayName("добавлять авторов в БД")
    void create() {
        var result = authorsDao.create(new Authors(null, OTHER_AUTHOR_NAME));

        assertEquals(OTHER_AUTHOR_NAME, result.name());
        assertNotNull(result.id());

        authorsDao.delete(result.id());
    }

    @Test
    @DisplayName("обновлять авторов в БД")
    void update() {
        var result = authorsDao.update(new Authors(EXISTING_AUTHOR_ID, OTHER_AUTHOR_NAME));

        assertEquals(OTHER_AUTHOR_NAME, result.name());
        assertNotNull(result.id());
    }

    @Test
    @DisplayName("выдовать исключение если id = null")
    void updateForNullId() {
        assertThrows(RuntimeException.class, () -> authorsDao.update(new Authors(null, null)));
    }

    @Test
    @DisplayName("удалять заданного автора по его id")
    void delete() {
        var authors = authorsDao.create(new Authors(null, OTHER_AUTHOR_NAME));

        assertTrue(authorsDao.exist(authors.id()));

        authorsDao.delete(authors.id());

        assertFalse(authorsDao.exist(authors.id()));
    }

    @Test
    @DisplayName("определять что автор с заданым id существует в БД")
    void exist() {
        assertTrue(authorsDao.exist(EXISTING_AUTHOR_ID));
        assertFalse(authorsDao.exist(EXISTING_AUTHOR_ID + OTHER_AUTHOR_ID));
    }

    @Test
    @DisplayName("возвращать ожидаемое количество авторов в БД")
    void count() {
        assertEquals(EXPECTED_AUTHOR_COUNT, authorsDao.count());
    }

    @Test
    @DisplayName("возвращать список авторов по id книги")
    void findByBookId() {
        var result = authorsDao.findByBookId(EXISTING_BOOKS_ID);

        assertNotNull(result);
        assertEquals(EXPECTED_AUTHOR_COUNT, result.size());
    }

    @Test
    @DisplayName("возвращать список авторов по имени книги")
    void findByName() {
        var result = authorsDao.findByName(EXISTING_AUTHOR_NAME);

        assertNotNull(result);
        assertEquals(EXISTING_AUTHOR_ID, result.id());
        assertEquals(EXISTING_AUTHOR_NAME, result.name());
    }

    @Test
    @DisplayName("возвращать список авторов по имени книги")
    void findByNameNotExist() {
        var result = authorsDao.findByName(OTHER_AUTHOR_NAME);

        assertNull(result);
    }
}