package ru.otus.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.entities.Book;

import java.util.List;

public interface BooksRepository extends CrudRepository<Book, String> {

    List<Book> findByName(String name);

}
