package ru.otus.controllers;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.dto.requests.AuthorsRequest;
import ru.otus.services.AuthorExistException;
import ru.otus.services.AuthorsService;

import java.io.PrintStream;

@ShellComponent
public class AuthorControllerImpl implements AuthorController {

    private final AuthorsService service;
    private final PrintStream out;

    public AuthorControllerImpl(AuthorsService service, PrintStream out) {
        this.service = service;
        this.out = out;
    }

    @Override
    @ShellMethod(value = "Create new author", key = {"author-c", "author-create"})
    public void create(String name) {
        try {
            out.println(
                    service.create(new AuthorsRequest(name))
                            .toString());
        } catch (AuthorExistException existException) {
            out.printf("Author %s already exist%n", name);
        }
    }

    @Override
    @ShellMethod(value = "Get author by id", key = {"author-get-id"})
    public void findById(Long id) {
        out.println(
                service.findById(id));
    }

    @Override
    @ShellMethod(value = "Get author by name", key = {"author-get-name"})
    public void findByName(String name) {
        out.println(service.findByName(name));
    }

    @Override
    @ShellMethod(value = "Update author", key = {"author-u", "author-update"})
    public void update(Long id, String name) {
        try {
            out.println(
                    service.update(id, new AuthorsRequest(name))
                            .toString());
        } catch (AuthorExistException existException) {
            out.printf("Author %s already exist%n", name);
        }
    }

    @Override
    @ShellMethod(value = "Delete author by id", key = {"author-d", "author-delete"})
    public void delete(Long id) {
        service.delete(id);
    }

    @Override
    @ShellMethod(value = "Get all author", key = {"author-all"})
    public void findAll() {
        service.findAll().forEach(out::println);
    }
}
