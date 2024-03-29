package ru.otus.repositories;

import ru.otus.entities.Book;

import java.util.List;

public interface BooksRepository extends Repository<Book, Long>{

    List<Book> getByName(String name);
}
