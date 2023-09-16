package ru.otus.controllers;

public interface GenresController {

    void create(String name);

    void findById(Long id);

    void findByName(String name);

    void update(Long id, String name);

    void delete(Long id);

    void findAll();
}
