package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.dao.decorators.JdbcDecorators;
import ru.otus.entities.Genres;
import ru.otus.mappers.rows.GenresMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Dao для работы с жанрами должно")
@JdbcTest
@Import({GenresDaoJdbc.class})
class GenresDaoJdbcTest {

    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final Long GENRE_ID = 1L;
    private static final String GENRE_NAME = "drama";
    private static final int TEST_INT = 1;
    private static final Genres TEST = new Genres(GENRE_ID, GENRE_NAME);
    private static final List<Genres> TEST_LIST = List.of(TEST);
    @MockBean
    private JdbcDecorators<Genres> jdbcDecorators;
    @MockBean
    private GenresMapper mapper;

    @Autowired
    private GenresDao genresDao;

    @Test
    @DisplayName("должен выполнить sql запрос к БД и вернуть жанр")
    void getById() {
        when(jdbcDecorators.findOne(any(), any(), any())).thenReturn(Optional.of(TEST));

        var result = genresDao.getById(GENRE_ID);

        assertEquals(TEST, result.orElse(null));
        verify(jdbcDecorators, times(1))
                .findOne(
                        eq("select id, name from genres where id = :id"),
                        any(),
                        eq(mapper));
    }

    @Test
    @DisplayName("должен выполнить sql запрос к БД и вернуть список жанров")
    void getAll() {
        when(jdbcDecorators.find(any(), any(), any())).thenReturn(TEST_LIST);

        var result = genresDao.getAll();

        assertEquals(TEST_LIST, result);
        verify(jdbcDecorators, times(1))
                .find(
                        eq("select id, name from genres"),
                        any(),
                        eq(mapper));
    }

    @Test
    @DisplayName("при создании выполнить sql запрос к БД и вернуть жанр")
    void create() {
        var genres = new Genres(null, GENRE_NAME);
        when(jdbcDecorators.executeAndReturnKey(any(), any())).thenReturn(GENRE_ID);
        when(jdbcDecorators.findOne(any(), any(), eq(mapper))).thenReturn(Optional.of(TEST));

        var result = genresDao.create(genres);

        assertEquals(TEST, result);
        verify(jdbcDecorators, times(1)).executeAndReturnKey(eq("insert into genres (name) values (:name)"), any());
        verify(jdbcDecorators, times(1)).findOne(any(), any(), eq(mapper));
    }

    @Test
    @DisplayName("при обновлении выдовать исключение если id = null и не обращаться к базе")
    void updateForNullId() {
        var genres = new Genres(null, GENRE_NAME);

        assertThrows(RuntimeException.class, () -> genresDao.update(genres));

        verify(jdbcDecorators, times(0)).executeAndReturnKey(any(), any());
        verify(jdbcDecorators, times(0)).findOne(any(), any(), any());
    }

    @Test
    @DisplayName("при обновлении выполнить sql запрос к БД и вернуть жанр")
    void update() {
        var genres = new Genres(GENRE_ID, GENRE_NAME);
        when(jdbcDecorators.executeAndReturnKey(any(), any())).thenReturn(GENRE_ID);
        when(jdbcDecorators.findOne(any(), any(), eq(mapper))).thenReturn(Optional.of(TEST));

        var result = genresDao.update(genres);

        assertEquals(TEST, result);
        verify(jdbcDecorators, times(1)).executeAndReturnKey(eq("update genres set name = :name where id = :id"), any());
        verify(jdbcDecorators, times(1)).findOne(any(String.class), any(), eq(mapper));
    }

    @Test
    @DisplayName("при удалении выполнить sql запрос к БД")
    void delete() {
        genresDao.delete(GENRE_ID);

        verify(jdbcDecorators, times(1)).execute(eq("delete from genres where id = :id"), any());
    }

    @Test
    @DisplayName("при  проверке на существование должен выполнить sql запрос к БД и вернуть логическое значение")
    void exist() {
        when(jdbcDecorators.getInt(eq("select count(id) from genres where id = :id"), any()))
                .thenReturn(Optional.of(TEST_INT));

        var result = genresDao.exist(GENRE_ID);

        assertTrue(result);
        verify(jdbcDecorators, times(1)).getInt(eq("select count(id) from genres where id = :id"), any());
    }

    @Test
    @DisplayName("при получении количества выполнить sql запрос к БД и вернуть число")
    void count() {
        when(jdbcDecorators.getInt(eq("select count(id) from genres"), any()))
                .thenReturn(Optional.of(TEST_INT));

        var result = genresDao.count();

        assertEquals(TEST_INT, result);
        verify(jdbcDecorators, times(1)).getInt(eq("select count(id) from genres"), any());
    }

    @Test
    @DisplayName("при поиске по id книги должен выполнить sql запрос к БД и вернуть список жанров")
    void findByBookId() {
        when(jdbcDecorators.find(any(), any(), any())).thenReturn(TEST_LIST);

        var result = genresDao.findByBookId(EXISTING_BOOKS_ID);

        assertEquals(TEST_LIST, result);
        verify(jdbcDecorators, times(1))
                .find(
                        eq("select genres.id, genres.name from genres genres left join books_genres on books_genres.genres = genres.id where books_genres.books = :books"),
                        any(),
                        eq(mapper));
    }

    @Test
    @DisplayName("при поиске по имени должен выполнить sql запрос к БД и вернуть жанр")
    void findByName() {
        when(jdbcDecorators.findOne(any(), any(), eq(mapper))).thenReturn(Optional.of(TEST));

        var result = genresDao.findByName(GENRE_NAME);

        assertEquals(TEST, result);
        verify(jdbcDecorators, times(1))
                .findOne(
                        eq("select id, name from genres where name = :name"),
                        any(),
                        eq(mapper));
    }
}