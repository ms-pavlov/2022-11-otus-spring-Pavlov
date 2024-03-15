package ru.otus.postgre.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.postgre.entities.Book;

public interface BooksRepository extends JpaRepository<Book, Long> {

}
