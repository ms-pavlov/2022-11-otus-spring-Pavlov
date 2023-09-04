package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.services.BooksService;

import java.io.PrintStream;
import java.util.List;
import java.util.logging.Logger;

@ShellComponent
public class BooksControllerImpl implements BooksController {

    private final static Logger log = Logger.getLogger(BooksControllerImpl.class.getName());

    private final BooksService service;
    private final PrintStream out;

    @Autowired
    public BooksControllerImpl(BooksService service, PrintStream out) {
        this.service = service;
        this.out = out;
    }

    @Override
    @ShellMethod(value = "Create new book", key = {"c", "create"})
    public void create(String name, List<String> authors, List<String> genres) {
        out.println(
                service.create(
                        new BooksRequest(name, authors, genres)));
    }

    @Override
    @ShellMethod(value = "Get book by id", key = {"id", "get-id"})
    public void findById(Long id) {
        out.println(
                service.findById(id));
    }

    @Override
    @ShellMethod(value = "Get books by name", key = {"name", "get-name"})
    public void findByName(String name) {
        service.findByName(name).forEach(out::println);
    }

    @Override
    @ShellMethod(value = "Get books by author", key = {"author", "get-author"})
    public void findByAuthor(String name) {
        service.findByAuthor(name).forEach(out::println);
    }

    @Override
    @ShellMethod(value = "Get books by genre", key = {"genre", "get-genre"})
    public void findByGenre(String name) {
        service.findByGenre(name).forEach(out::println);
    }

    @Override
    @ShellMethod(value = "Get all books", key = {"all", "get-all"})
    public void findAll() {
        service.findAll().forEach(out::println);
    }

    @Override
    @ShellMethod(value = "Update book", key = {"u", "update"})
    public void update(Long id, String name, List<String> authors, List<String> genres) {
        out.println(
                service.update(id, new BooksRequest(name, authors, genres)));
    }

    @Override
    @ShellMethod(value = "Delete book by id", key = {"d", "delete"})
    public void delete(Long id) {
        service.delete(id);
    }
}
