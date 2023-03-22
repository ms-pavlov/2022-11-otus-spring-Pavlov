package ru.otus.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.dao.decorators.JdbcDecorators;
import ru.otus.entities.Genres;
import ru.otus.mappers.rows.GenresMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class GenresDaoJdbc implements GenresDao {

    private final JdbcDecorators<Genres> jdbcDecorators;
    private final GenresMapper mapper;

    @Autowired
    public GenresDaoJdbc(
            JdbcDecorators<Genres> jdbcDecorators,
            GenresMapper mapper) {
        this.jdbcDecorators = jdbcDecorators;
        this.mapper = mapper;
    }

    @Override
    public Optional<Genres> getById(long id) {
        return jdbcDecorators.findOne(
                "select id, name from genres where id = :id",
                Map.of("id", id),
                mapper);
    }

    @Override
    public List<Genres> getAll() {
        return jdbcDecorators.find(
                "select id, name from genres",
                Map.of(),
                mapper);
    }

    @Override
    public Genres create(Genres genre) {
        return getById(
                jdbcDecorators.executeAndReturnKey(
                        "insert into genres (name) values (:name)",
                        Map.of("name", genre.name())))
                .orElse(null);
    }

    @Override
    public Genres update(Genres genre) {
        if (genre.id() == null) {
            throw new RuntimeException("Can't update Genres with id = null");
        }
        return getById(
                jdbcDecorators.executeAndReturnKey(
                        "update genres set name = :name where id = :id",
                        Map.of(
                                "name", genre.name(),
                                "id", genre.id())))
                .orElse(null);
    }

    @Override
    public void delete(long id) {
        jdbcDecorators.execute(
                "delete from genres where id = :id",
                Map.of("id", id));
    }

    @Override
    public boolean exist(long id) {
        return jdbcDecorators.getInt(
                        "select count(id) from genres where id = :id",
                        Map.of("id", id))
                .map(count -> count > 0)
                .orElse(false);
    }

    @Override
    public int count() {
        return jdbcDecorators
                .getInt(
                        "select count(id) from genres",
                        Map.of())
                .orElse(0);
    }

    @Override
    public List<Genres> findByBookId(Long id) {
        return jdbcDecorators.find(
                "select genres.id, genres.name from genres genres left join books_genres on books_genres.genres = genres.id where books_genres.books = :books",
                Map.of("books", id),
                mapper);
    }

    @Override
    public Genres findByName(String name) {
        return jdbcDecorators.findOne(
                        "select id, name from genres where name = :name",
                        Map.of("name", name),
                        mapper)
                .orElse(null);
    }
}
