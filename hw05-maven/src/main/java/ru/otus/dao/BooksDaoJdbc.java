package ru.otus.dao;

import org.springframework.stereotype.Component;
import ru.otus.dao.decorators.JdbcDecorators;
import ru.otus.entities.Books;
import ru.otus.mappers.rows.BooksMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class BooksDaoJdbc implements BooksDao {
    private final static Logger log = Logger.getLogger(BooksDaoJdbc.class.getName());
    private final JdbcDecorators<Books> jdbcDecorators;
    private final BooksMapper mapper;
    private final AuthorsDao authorsDao;
    private final GenresDao genresDao;

    public BooksDaoJdbc(
            JdbcDecorators<Books> jdbcDecorators,
            BooksMapper mapper,
            AuthorsDao authorsDao,
            GenresDao genresDao) {
        this.jdbcDecorators = jdbcDecorators;
        this.mapper = mapper;
        this.authorsDao = authorsDao;
        this.genresDao = genresDao;
    }

    @Override
    public Optional<Books> getById(long id) {
        return jdbcDecorators
                .findOne(
                        "select id, name from books where id = :id",
                        Map.of("id", id),
                        mapper)
                .map(books -> {
                    loadLinkedEntity(books);
                    return books;
                });
    }

    @Override
    public List<Books> getAll() {
        return jdbcDecorators.find(
                        "select id, name from books",
                        Map.of(),
                        mapper)
                .stream()
                .peek(this::loadLinkedEntity)
                .toList();
    }

    @Override
    public Books create(Books book) {
        long books_id = jdbcDecorators.executeAndReturnKey(
                "insert into books (name) values (:name)",
                Map.of("name", book.getName()));
        addLinkedEntity(book, books_id);

        return getById(books_id).orElse(null);
    }

    @Override
    public Books update(Books book) {
        if (book.getId() == null) {
            throw new RuntimeException("Can't update Books with id = null");
        }
        long books_id = jdbcDecorators.executeAndReturnKey(
                "update books set name = :name where id = :id",
                Map.of(
                        "name", book.getName(),
                        "id", book.getId()));

        deleteLinkedEntity(books_id);
        addLinkedEntity(book, books_id);

        return getById(books_id).orElse(null);
    }

    @Override
    public void delete(long id) {
        jdbcDecorators.executeAndReturnKey(
                "delete from books where id = :id",
                Map.of("id", id));
    }

    @Override
    public boolean exist(long id) {
        return jdbcDecorators.getInt(
                        "select count(id) from books where id = :id",
                        Map.of("id", id))
                .map(count -> count > 0)
                .orElse(false);
    }

    @Override
    public int count() {
        return jdbcDecorators
                .getInt(
                        "select count(id) from books",
                        Map.of())
                .orElse(0);
    }

    @Override
    public List<Books> getByName(String name) {
        return jdbcDecorators.find(
                        "select id, name from books where books.name = :name",
                        Map.of("name", name),
                        mapper)
                .stream()
                .peek(this::loadLinkedEntity)
                .toList();
    }

    @Override
    public List<Books> getByAuthor(String name) {
        return jdbcDecorators.find(
                        "select books.id, books.name " +
                        " from books " +
                        "   left join books_authors on books_authors.books = books.id " +
                        "   left join authors on authors.id = books_authors.authors " +
                        " where authors.name = :name",
                        Map.of("name", name),
                        mapper)
                .stream()
                .peek(this::loadLinkedEntity)
                .toList();
    }

    @Override
    public List<Books> getByGenre(String name) {
        return jdbcDecorators.find(
                        "select books.id, books.name " +
                        " from books " +
                        "   left join books_genres on books_genres.books = books.id " +
                        "   left join genres on genres.id = books_genres.genres " +
                        " where genres.name = :name",
                        Map.of("name", name),
                        mapper)
                .stream()
                .peek(this::loadLinkedEntity)
                .toList();
    }

    private void deleteLinkedEntity(Long books) {
        jdbcDecorators.execute(
                "delete from books_genres where books = :books",
                Map.of("books", books));
        jdbcDecorators.execute(
                "delete from books_authors where books = :books",
                Map.of("books", books));
    }

    private void addLinkedEntity(Books books, Long id) {
        books.getAuthors().forEach(
                author -> {
                    Long author_id = author.id();
                    if (author.id() == null) {
                        author_id = jdbcDecorators.executeAndReturnKey(
                                "insert into authors (name) values (:name)",
                                Map.of("name", author.name()));
                    }

                    jdbcDecorators.execute(
                            "insert into books_authors (books, authors) values (:books, :authors)",
                            Map.of(
                                    "books", id,
                                    "authors", author_id));
                });
        books.getGenres().forEach(
                genre -> {
                    Long genres_id = genre.id();
                    if (genre.id() == null) {
                        genres_id = jdbcDecorators.executeAndReturnKey(
                                "insert into genres (name) values (:name)",
                                Map.of("name", genre.name()));
                    }

                    jdbcDecorators.execute(
                            "insert into books_genres (books, genres) values (:books, :genres)",
                            Map.of(
                                    "books", id,
                                    "genres", genres_id));
                });
    }

    private void loadLinkedEntity(Books books) {
        books.setAuthors(authorsDao.findByBookId(books.getId()));
        books.setGenres(genresDao.findByBookId(books.getId()));
    }
}
