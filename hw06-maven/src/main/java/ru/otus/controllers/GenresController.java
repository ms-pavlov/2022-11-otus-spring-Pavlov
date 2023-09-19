package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.dto.requests.GenresRequest;
import ru.otus.services.GenreExistException;
import ru.otus.services.GenresService;

import java.io.PrintStream;

@ShellComponent
public class GenresController {

    private final GenresService service;
    private final PrintStream out;

    @Autowired
    public GenresController(GenresService service, PrintStream out) {
        this.service = service;
        this.out = out;
    }

    @ShellMethod(value = "Create new genre", key = {"genre-c", "genre-create"})
    public void create(String name) {
        try {
            out.println(
                    service.create(new GenresRequest(name))
                            .toString());
        } catch (GenreExistException existException) {
            out.printf("Genre %s already exist%n", name);
        }
    }

    @ShellMethod(value = "Get genre by id", key = {"genre-get-id"})
    public void findById(Long id) {
        out.println(
                service.findById(id));
    }

    @ShellMethod(value = "Get genre by name", key = {"genre-get-name"})
    public void findByName(String name) {
        out.println(service.findByName(name));
    }

    @ShellMethod(value = "Update genre", key = {"genre-u", "genre-update"})
    public void update(Long id, String name) {
        try {
            out.println(
                    service.update(id, new GenresRequest(name))
                            .toString());
        } catch (GenreExistException existException) {
            out.printf("Genre %s already exist%n", name);
        }
    }

    @ShellMethod(value = "Delete genre by id", key = {"genre-d", "genre-delete"})
    public void delete(Long id) {
        service.delete(id);
    }

    @ShellMethod(value = "Get all genre", key = {"genre-all"})
    public void findAll() {
        service.findAll().forEach(out::println);
    }
}
