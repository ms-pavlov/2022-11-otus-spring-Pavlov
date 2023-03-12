package ru.otus.mappers.rows;

import org.springframework.stereotype.Component;
import ru.otus.entities.Books;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class BooksMapperImpl implements BooksMapper {
    @Override
    public Books mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Books(
                rs.getLong("id"),
                rs.getString("name"),
                new ArrayList<>(),
                new ArrayList<>());
    }


}
