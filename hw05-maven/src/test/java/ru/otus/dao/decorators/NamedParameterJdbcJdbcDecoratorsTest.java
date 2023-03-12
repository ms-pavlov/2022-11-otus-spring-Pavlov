package ru.otus.dao.decorators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.dao.AuthorsDaoJdbc;
import ru.otus.entities.Authors;
import ru.otus.mappers.rows.AuthorsMapper;
import ru.otus.mappers.rows.AuthorsMapperImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Декоратор для Jdbc")
@JdbcTest
@Import({NamedParameterJdbcJdbcDecorators.class})
@ExtendWith(MockitoExtension.class)
class NamedParameterJdbcJdbcDecoratorsTest {

    private static final Integer EXPECTED_AUTHOR_COUNT = 1;
    private static final Long EXISTING_AUTHOR_ID = 1L;
    private static final String EXISTING_AUTHOR_NAME = "Ivan";
    private static final String OTHER_AUTHOR_NAME = "Petr";

    @Autowired
    @Spy
    private JdbcDecorators<Authors> jdbcDecorators;

    @MockBean
    private AuthorsMapper mapper;

    @BeforeEach
    void setUp() throws SQLException {
        when(mapper.mapRow(any(ResultSet.class), any(Integer.class)))
                .thenAnswer(
                        invocationOnMock -> new AuthorsMapperImpl().mapRow(
                                invocationOnMock.getArgument(0),
                                invocationOnMock.getArgument(1)));
    }

    @Test
    @DisplayName("должен выполнять sql запрос на изменение и возвращать id изменненной записи")
    void executeAndReturnKey() {
        var dao = new AuthorsDaoJdbc(jdbcDecorators, new AuthorsMapperImpl());

        var id = jdbcDecorators.executeAndReturnKey("insert into authors (name) values (:name)", Map.of("name", OTHER_AUTHOR_NAME));

        var authors = dao.getById(id).orElse(null);

        assertNotNull(authors);
        assertEquals(OTHER_AUTHOR_NAME, authors.name());
        assertNotNull(authors.id());

        dao.delete(id);
    }

    @Test
    @DisplayName("должен выбирать несколько записей")
    void find() {
        var result = jdbcDecorators.find(
                "select id, name from authors where id = :id",
                Map.of("id", EXISTING_AUTHOR_ID),
                mapper);

        assertNotNull(result);
        assertEquals(EXPECTED_AUTHOR_COUNT, result.size());
    }

    @Test
    @DisplayName("должен выбирать одну запись")
    void findOne() {
        var result = jdbcDecorators.findOne(
                "select id, name from authors where id = :id",
                Map.of("id", EXISTING_AUTHOR_ID),
                mapper).orElse(null);

        assertNotNull(result);
        assertEquals(EXISTING_AUTHOR_ID, result.id());
        assertEquals(EXISTING_AUTHOR_NAME, result.name());
    }

    @Test
    @DisplayName("должен возвращать null если нет результата")
    void findOneNotExist() {
        var result = jdbcDecorators.findOne(
                "select id, name from authors where id = :id",
                Map.of("id", EXISTING_AUTHOR_ID + 100L),
                mapper).orElse(null);

        assertNull(result);
    }

    @Test
    @DisplayName("должен выбирать получать число из sql запроса")
    void getInt() {
        var result = jdbcDecorators
                .getInt(
                        "select count(id) from authors",
                        Map.of())
                .orElse(0);

        assertNotNull(result);
        assertEquals(EXPECTED_AUTHOR_COUNT, result);
    }

    @Test
    @DisplayName("должен выполнять sql запрос")
    void execute() {
        var param = Map.of("name", OTHER_AUTHOR_NAME);
        var sql = "insert into genres (name) values (:name)";
        jdbcDecorators.execute(sql, param);

        verify(jdbcDecorators, times(1)).execute(sql, param);


    }
}