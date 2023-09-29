package ru.otus.controllers;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.services.CommentsService;

import java.io.PrintStream;
import java.util.stream.Collectors;

@ShellComponent
public class CommentsController {

    private final CommentsService service;
    private final PrintStream out;

    public CommentsController(CommentsService service, PrintStream out) {
        this.service = service;
        this.out = out;
    }

    @ShellMethod(value = "Create new comment", key = {"comment-c", "comment-create"})
    public void create(String comment, Long bookId) {
        out.println(
                service.create(new CommentsRequest(comment, bookId))
                        .toString());
    }

    @ShellMethod(value = "Get comment by id", key = {"comment-get-id"})
    public void findById(Long id) {
        out.println(
                service.findById(id));
    }

    @ShellMethod(value = "Get comment by book", key = {"comment-get-book"})
    public void findByBook(Long bookId) {
        var bookWithComments = service.findByBookId(bookId);

        if(bookWithComments.isEmpty()) {
            out.println("No Comments");
            return;
        }

        out.println(bookWithComments.getBook());
        out.println("Comments: ");
        bookWithComments.getComments()
                .forEach(out::println);
    }

    @ShellMethod(value = "Update comment", key = {"comment-u", "comment-update"})
    public void update(Long id, String comment, Long bookId) {
        out.println(
                service.update(id, new CommentsRequest(comment, bookId))
                        .toString());
    }

    @ShellMethod(value = "Delete comment by id", key = {"comment-d", "comment-delete"})
    public void delete(Long id) {
        service.delete(id);
    }

    @ShellMethod(value = "Get all comment", key = {"comment-all"})
    public void findAll() {
        service.findAll()
                .stream()
                .collect(Collectors.groupingBy(CommentsResponse::getBook))
                .forEach((key, value) -> {
                    out.println(key);
                    out.println("Comments: ");
                    value.forEach(out::println);
                });
    }

}
