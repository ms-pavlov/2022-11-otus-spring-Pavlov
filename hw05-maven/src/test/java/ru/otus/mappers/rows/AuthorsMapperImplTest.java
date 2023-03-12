package ru.otus.mappers.rows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.entities.Authors;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@DisplayName("Mapper для работы с авторами должен")
@ExtendWith(MockitoExtension.class)
class AuthorsMapperImplTest {
    private static final Long AUTHOR_ID = 2L;
    private static final String AUTHOR_NAME = "Ivan";

    @Mock
    private ResultSet rs;

    private final AuthorsMapper mapper = new AuthorsMapperImpl();

    @Test
    @DisplayName("разбирать ResultSet и собирать полученные значения в Authors")
    void mapRow() throws SQLException {
        when(rs.getLong("id")).thenReturn(AUTHOR_ID);
        when(rs.getString("name")).thenReturn(AUTHOR_NAME);

        Authors result = mapper.mapRow(rs, 0);

        assertNotNull(result);
        assertEquals(AUTHOR_ID, result.id());
        assertEquals(AUTHOR_NAME, result.name());
    }
}