package ru.otus.mappers.rows;

import org.springframework.stereotype.Component;
import ru.otus.entities.Authors;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthorsMapperImpl implements AuthorsMapper {
    @Override
    public Authors mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Authors(
                rs.getLong("id"),
                rs.getString("name"));
    }
}
