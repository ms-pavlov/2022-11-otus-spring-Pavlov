package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.dao.decorators.NamedParameterJdbcJdbcDecorators;
import ru.otus.entities.Genres;
import ru.otus.mappers.rows.GenresMapperImpl;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dao для работы с жанрами должно")
@JdbcTest
@Import({GenresDaoJdbc.class, NamedParameterJdbcJdbcDecorators.class, GenresMapperImpl.class})
class GenresDaoJdbcIntegrationTest {
    private static final Integer EXPECTED_GENRES_COUNT = 1;
    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final Long EXISTING_GENRES_ID = 1L;
    private static final Long OTHER_GENRES_ID = 200L;
    private static final String EXISTING_GENRES_NAME = "drama";
    private static final String OTHER_GENRES_NAME = "comedy";

    @Autowired
    private GenresDao genresDao;

    @Test
    @DisplayName("возвращать жанр по его id")
    void getById() {
        var result = genresDao.getById(EXISTING_GENRES_ID);

        assertEquals(EXISTING_GENRES_NAME, result.map(Genres::name).orElse(null));
    }

    @Test
    @DisplayName("возвращать ожидаемый список жанров")
    void getAll() {
        var result = genresDao.getAll();

        assertEquals(EXPECTED_GENRES_COUNT, result.size());
    }

    @Test
    @DisplayName("добавлять жанры в БД")
    void create() {
        var result = genresDao.create(new Genres(null, OTHER_GENRES_NAME));

        assertEquals(OTHER_GENRES_NAME, result.name());
        assertNotNull(result.id());

        genresDao.delete(result.id());
    }

    @Test
    @DisplayName("обновлять жанры в БД")
    void update() {
        var result = genresDao.update(new Genres(EXISTING_GENRES_ID, OTHER_GENRES_NAME));

        assertEquals(OTHER_GENRES_NAME, result.name());
        assertNotNull(result.id());
    }

    @Test
    @DisplayName("выдовать исключение если id = null")
    void updateForNullId() {
        assertThrows(RuntimeException.class, () -> genresDao.update(new Genres(null, null)));
    }

    @Test
    @DisplayName("удалять заданного жанр по его id")
    void delete() {
        var genres = genresDao.create(new Genres(null, OTHER_GENRES_NAME));

        assertTrue(genresDao.exist(genres.id()));

        genresDao.delete(genres.id());

        assertFalse(genresDao.exist(genres.id()));
    }

    @Test
    @DisplayName("определять что жанр с заданым id существует в БД")
    void exist() {
        assertTrue(genresDao.exist(EXISTING_GENRES_ID));
        assertFalse(genresDao.exist(EXISTING_GENRES_ID + OTHER_GENRES_ID));
    }

    @Test
    @DisplayName("возвращать ожидаемое количество жанров в БД")
    void count() {
        assertEquals(EXPECTED_GENRES_COUNT, genresDao.count());
    }

    @Test
    @DisplayName("возвращать список жанров по id книги")
    void findByBookId() {
        var result = genresDao.findByBookId(EXISTING_BOOKS_ID);

        assertNotNull(result);
        assertEquals(EXPECTED_GENRES_COUNT, result.size());
    }

    @Test
    void findByName() {
        var result = genresDao.findByName(EXISTING_GENRES_NAME);

        assertNotNull(result);
        assertEquals(EXISTING_GENRES_ID, result.id());
        assertEquals(EXISTING_GENRES_NAME, result.name());
    }
}