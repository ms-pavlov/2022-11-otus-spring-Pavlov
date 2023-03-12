package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.dao.decorators.JdbcDecorators;
import ru.otus.entities.Authors;
import ru.otus.mappers.rows.AuthorsMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Dao для работы с авторами должно")
@JdbcTest
@Import({AuthorsDaoJdbc.class})
class AuthorsDaoJdbcTest {
    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final Long AUTHOR_ID = 1L;
    private static final String AUTHOR_NAME = "Ivan";
    private static final int TEST_INT = 1;
    private static final Authors TEST = new Authors(AUTHOR_ID, AUTHOR_NAME);
    private static final List<Authors> TEST_LIST = List.of(TEST);


    @MockBean
    private JdbcDecorators<Authors> jdbcDecorators;
    @MockBean
    private AuthorsMapper mapper;

    @Autowired
    private AuthorsDao authorsDao;

    @Test
    @DisplayName("должен выполнить sql запрос к БД и вернуть автора")
    void getById() {
        when(jdbcDecorators.findOne(any(), any(), any())).thenReturn(Optional.of(TEST));

        var result = authorsDao.getById(AUTHOR_ID);

        assertEquals(TEST, result.orElse(null));
        verify(jdbcDecorators, times(1))
                .findOne(
                        eq("select id, name from authors where id = :id"),
                        any(),
                        eq(mapper));
    }

    @Test
    @DisplayName("должен выполнить sql запрос к БД и вернуть список авторов")
    void getAll() {
        when(jdbcDecorators.find(any(), any(), any())).thenReturn(TEST_LIST);

        var result = authorsDao.getAll();

        assertEquals(TEST_LIST, result);
        verify(jdbcDecorators, times(1))
                .find(
                        eq("select id, name from authors"),
                        any(),
                        eq(mapper));
    }

    @Test
    @DisplayName("при создании выполнить sql запрос к БД и вернуть автора")
    void create() {
        var authors = new Authors(null, AUTHOR_NAME);
        when(jdbcDecorators.executeAndReturnKey(any(), any())).thenReturn(AUTHOR_ID);
        when(jdbcDecorators.findOne(any(), any(), eq(mapper))).thenReturn(Optional.of(TEST));

        var result = authorsDao.create(authors);

        assertEquals(TEST, result);
        verify(jdbcDecorators, times(1)).executeAndReturnKey(eq("insert into authors (name) values (:name)"), any());
        verify(jdbcDecorators, times(1)).findOne(any(), any(), eq(mapper));
    }

    @Test
    @DisplayName("при обновлении выдовать исключение если id = null и не обращаться к базе")
    void updateForNullId() {
        var authors = new Authors(null, AUTHOR_NAME);

        assertThrows(RuntimeException.class, () -> authorsDao.update(authors));

        verify(jdbcDecorators, times(0)).executeAndReturnKey(any(), any());
        verify(jdbcDecorators, times(0)).findOne(any(), any(), any());
    }

    @Test
    @DisplayName("при обновлении выполнить sql запрос к БД и вернуть автора")
    void update() {
        var authors = new Authors(AUTHOR_ID, AUTHOR_NAME);
        when(jdbcDecorators.executeAndReturnKey(any(), any())).thenReturn(AUTHOR_ID);
        when(jdbcDecorators.findOne(any(), any(), eq(mapper))).thenReturn(Optional.of(TEST));

        var result = authorsDao.update(authors);

        assertEquals(TEST, result);
        verify(jdbcDecorators, times(1)).executeAndReturnKey(eq("update authors set name = :name where id = :id"), any());
        verify(jdbcDecorators, times(1)).findOne(any(String.class), any(), eq(mapper));
    }

    @Test
    @DisplayName("при удалении выполнить sql запрос к БД")
    void delete() {
        authorsDao.delete(AUTHOR_ID);

        verify(jdbcDecorators, times(1)).execute(eq("delete from authors where id = :id"), any());
    }

    @Test
    @DisplayName("при проверке на существование должен выполнить sql запрос к БД и вернуть логическое значение")
    void exist() {
        when(jdbcDecorators.getInt(eq("select count(id) from authors where id = :id"), any()))
                .thenReturn(Optional.of(TEST_INT));

        var result = authorsDao.exist(AUTHOR_ID);

        assertTrue(result);
        verify(jdbcDecorators, times(1)).getInt(eq("select count(id) from authors where id = :id"), any());
    }

    @Test
    @DisplayName("при получении количества выполнить sql запрос к БД и вернуть число")
    void count() {
        when(jdbcDecorators.getInt(eq("select count(id) from authors"), any()))
                .thenReturn(Optional.of(TEST_INT));

        var result = authorsDao.count();

        assertEquals(TEST_INT, result);
        verify(jdbcDecorators, times(1)).getInt(eq("select count(id) from authors"), any());
    }

    @Test
    @DisplayName("при поиске по id книги должен выполнить sql запрос к БД и вернуть список авторов")
    void findByBookId() {
        when(jdbcDecorators.find(any(), any(), any())).thenReturn(TEST_LIST);

        var result = authorsDao.findByBookId(EXISTING_BOOKS_ID);

        assertEquals(TEST_LIST, result);
        verify(jdbcDecorators, times(1))
                .find(
                        eq("select id, name from authors authors left join books_authors on books_authors.authors = authors.id where books_authors.books = :books"),
                        any(),
                        eq(mapper));
    }

    @Test
    @DisplayName("при поиске по имени должен выполнить sql запрос к БД и вернуть автора")
    void findByName() {
        when(jdbcDecorators.findOne(any(), any(), eq(mapper))).thenReturn(Optional.of(TEST));

        var result = authorsDao.findByName(AUTHOR_NAME);

        assertEquals(TEST, result);
        verify(jdbcDecorators, times(1))
                .findOne(
                        eq("select id, name from authors where name = :name"),
                        any(),
                        eq(mapper));
    }
}