package ru.otus.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.dao.decorators.JdbcDecorators;
import ru.otus.entities.Authors;
import ru.otus.mappers.rows.AuthorsMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class AuthorsDaoJdbc implements AuthorsDao {

    private final JdbcDecorators<Authors> jdbcDecorators;
    private final AuthorsMapper mapper;

    @Autowired
    public AuthorsDaoJdbc(
            JdbcDecorators<Authors> jdbcDecorators,
            AuthorsMapper mapper) {
        this.jdbcDecorators = jdbcDecorators;
        this.mapper = mapper;
    }

    @Override
    public Optional<Authors> getById(long id) {
        return jdbcDecorators.findOne(
                "select id, name from authors where id = :id",
                Map.of("id", id),
                mapper);
    }

    @Override
    public List<Authors> getAll() {
        return jdbcDecorators.find(
                "select id, name from authors",
                Map.of(),
                mapper);
    }

    @Override
    public Authors create(Authors author) {
        return getById(
                jdbcDecorators.executeAndReturnKey(
                        "insert into authors (name) values (:name)",
                        Map.of("name", author.name())))
                .orElse(null);
    }

    @Override
    public Authors update(Authors author) {
        if (author.id() == null) {
            throw new RuntimeException("Can't update Authors with id = null");
        }
        return getById(
                jdbcDecorators.executeAndReturnKey(
                        "update authors set name = :name where id = :id",
                        Map.of(
                                "name", author.name(),
                                "id", author.id())))
                .orElse(null);
    }

    @Override
    public void delete(long id) {
        jdbcDecorators.execute(
                "delete from authors where id = :id",
                Map.of("id", id));
    }

    @Override
    public boolean exist(long id) {
        return jdbcDecorators.getInt(
                        "select count(id) from authors where id = :id",
                        Map.of("id", id)).map(count -> count > 0)
                .orElse(false);
    }

    @Override
    public int count() {
        return jdbcDecorators
                .getInt(
                        "select count(id) from authors",
                        Map.of())
                .orElse(0);
    }

    @Override
    public List<Authors> findByBookId(Long id) {
        return jdbcDecorators.find(
                "select id, name from authors authors left join books_authors on books_authors.authors = authors.id where books_authors.books = :books",
                Map.of("books", id),
                mapper);
    }

    @Override
    public Authors findByName(String name) {
        return jdbcDecorators.findOne(
                        "select id, name from authors where name = :name",
                        Map.of("name", name),
                        mapper)
                .orElse(null);
    }
}
