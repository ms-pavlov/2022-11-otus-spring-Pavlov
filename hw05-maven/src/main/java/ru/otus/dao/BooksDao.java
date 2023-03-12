package ru.otus.dao;

import ru.otus.entities.Books;

import java.util.List;

public interface BooksDao extends Dao<Books> {
    List<Books> getByName(String name);

    List<Books> getByAuthor(String name);

    List<Books> getByGenre(String name);
}
