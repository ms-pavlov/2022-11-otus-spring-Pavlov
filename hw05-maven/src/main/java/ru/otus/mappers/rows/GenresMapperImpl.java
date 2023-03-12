package ru.otus.mappers.rows;

import org.springframework.stereotype.Component;
import ru.otus.entities.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenresMapperImpl implements GenresMapper {
    @Override
    public Genres mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Genres(
                rs.getLong("id"),
                rs.getString("name"));
    }
}
