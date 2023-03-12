package ru.otus.dao.decorators;

import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JdbcDecorators<T> {

    Long executeAndReturnKey(String sql, Map<String, ?> parameters);

    void execute(String sql, Map<String, ?> parameters);

    List<T> find(String sql, Map<String, ?> parameters, RowMapper<T> mapper);

    Optional<T> findOne(String sql, Map<String, ?> parameters, RowMapper<T> mapper);

    Optional<Integer> getInt(String sql, Map<String, ?> parameters);

}
