package ru.otus.dao.decorators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class NamedParameterJdbcJdbcDecorators<T> implements JdbcDecorators<T> {

    private final NamedParameterJdbcOperations namedParameter;

    @Autowired
    public NamedParameterJdbcJdbcDecorators(NamedParameterJdbcOperations namedParameter) {
        this.namedParameter = namedParameter;
    }

    @Override
    public Long executeAndReturnKey(String sql, Map<String, ?> parameters) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameter.update(sql, new MapSqlParameterSource(parameters), keyHolder);
        return Optional.of(keyHolder)
                .map(KeyHolder::getKey)
                .map(Number::longValue)
                .orElse(null);
    }

    @Override
    public void execute(String sql, Map<String, ?> parameters) {
        namedParameter.update(sql, new MapSqlParameterSource(parameters));
    }

    @Override
    public List<T> find(String sql, Map<String, ?> parameters, RowMapper<T> mapper) {
        return namedParameter.query(sql, parameters, mapper);
    }

    @Override
    public Optional<T> findOne(String sql, Map<String, ?> parameters, RowMapper<T> mapper) {
        return namedParameter.queryForStream(sql, parameters, mapper).findFirst();
    }

    @Override
    public Optional<Integer> getInt(String sql, Map<String, ?> parameters) {
        return Optional.ofNullable(namedParameter.queryForObject(sql, parameters, Integer.class));
    }

}
