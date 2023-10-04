package ru.otus.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.entities.Book;

import java.util.List;

public interface BooksRepository extends CrudRepository<Book, Long> {

    @Query("SELECT b FROM Book b JOIN FETCH b.comments WHERE b.name = :name")
    List<Book> findByName(String name);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.comments")
    List<Book> find();
}
