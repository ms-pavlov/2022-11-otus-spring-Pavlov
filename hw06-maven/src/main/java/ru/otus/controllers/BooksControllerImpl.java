package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.services.BooksService;

import java.io.PrintStream;
import java.util.List;

@ShellComponent
public class BooksControllerImpl implements BooksController {

    private final BooksService service;
    private final PrintStream out;

    @Autowired
    public BooksControllerImpl(BooksService service, PrintStream out) {
        this.service = service;
        this.out = out;
    }

    @Override
    @ShellMethod(value = "Create new book", key = {"book-c", "book-create"})
    public void create(String name, List<String> authors, List<String> genres) {
        out.println(
                service.create(
                        new BooksRequest(name, authors, genres))
                        .toString());
    }

    @Override
    @ShellMethod(value = "Get book by id", key = {"book-get-id"})
    public void findById(Long id) {
        out.println(
                service.findById(id));
    }

    @Override
    @ShellMethod(value = "Get books by name", key = {"book-get-name"})
    public void findByName(String name) {
        service.findByName(name).forEach(out::println);
    }

    @Override
    @ShellMethod(value = "Get all books", key = {"book-all"})
    public void findAll() {
        service.findAll().forEach(out::println);
    }

    @Override
    @ShellMethod(value = "Update book", key = {"book-u", "book-update"})
    public void update(Long id, String name, List<String> authors, List<String> genres) {
        out.println(
                service.update(id, new BooksRequest(name, authors, genres))
                        .toString());
    }

    @Override
    @ShellMethod(value = "Delete book by id", key = {"book-d", "book-delete"})
    public void delete(Long id) {
        service.delete(id);
    }
}
