package ru.otus.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;

import java.util.List;

public interface BooksRepository extends CrudRepository<Book, String> {

    List<Book> findByName(String name);

    boolean existsByAuthorsContains(Author author);

    boolean existsByGenresContains(Genre genre);

}
