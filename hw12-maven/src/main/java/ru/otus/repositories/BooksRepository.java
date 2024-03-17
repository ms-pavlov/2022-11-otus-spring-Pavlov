package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.entities.Book;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE UPPER(b.name) like UPPER(:name)")
    List<Book> findByName(String name);

}
