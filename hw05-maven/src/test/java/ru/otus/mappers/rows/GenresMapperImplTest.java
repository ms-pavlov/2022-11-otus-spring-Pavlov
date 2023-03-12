package ru.otus.mappers.rows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.entities.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@DisplayName("Mapper для работы с жанрами должен")
@ExtendWith(MockitoExtension.class)
class GenresMapperImplTest {
    private static final Long GENRES_ID = 2L;
    private static final String GENRES_NAME = "drama";

    @Mock
    private ResultSet rs;

    private final GenresMapper mapper = new GenresMapperImpl();

    @Test
    @DisplayName("разбирать ResultSet и собирать полученные значения в Genres")
    void mapRow() throws SQLException {
        when(rs.getLong("id")).thenReturn(GENRES_ID);
        when(rs.getString("name")).thenReturn(GENRES_NAME);

        Genres result = mapper.mapRow(rs, 0);

        assertNotNull(result);
        assertEquals(GENRES_ID, result.id());
        assertEquals(GENRES_NAME, result.name());
    }
}