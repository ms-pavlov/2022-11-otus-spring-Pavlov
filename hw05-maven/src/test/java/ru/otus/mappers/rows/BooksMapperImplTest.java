package ru.otus.mappers.rows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.entities.Books;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@DisplayName("Mapper для работы с книгами должен")
@ExtendWith(MockitoExtension.class)
class BooksMapperImplTest {
    private static final Long BOOKS_ID = 2L;
    private static final String BOOKS_NAME = "BOOKS_NAME";

    @Mock
    private ResultSet rs;

    private final BooksMapper mapper = new BooksMapperImpl();

    @Test
    @DisplayName("разбирать ResultSet и собирать полученные значения в Books")
    void mapRow() throws SQLException {
        when(rs.getLong("id")).thenReturn(BOOKS_ID);
        when(rs.getString("name")).thenReturn(BOOKS_NAME);

        Books result = mapper.mapRow(rs, 0);

        assertNotNull(result);
        assertEquals(BOOKS_ID, result.getId());
        assertEquals(BOOKS_NAME, result.getName());
    }
}