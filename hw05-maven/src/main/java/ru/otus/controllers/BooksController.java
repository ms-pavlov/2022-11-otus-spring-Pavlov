package ru.otus.controllers;

import java.util.List;

public interface BooksController {

    void create(String name, List<String> authors, List<String> genres);

    void findById(Long id);

    void findByName(String name);

    void findByAuthor(String name);

    void findByGenre(String name);

    void update(Long id, String name, List<String> authors, List<String> genres);

    void delete(Long id);

    void findAll();
}
