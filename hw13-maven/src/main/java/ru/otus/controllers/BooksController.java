package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.services.BooksService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BooksController {

    private final BooksService service;

    @GetMapping("/api/v1/book/{id}")
    public BooksResponse findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @GetMapping("/api/v1/book/{page}/{size}")
    public Page<BooksResponse> findPage(
            @PathVariable(value = "page") Integer page,
            @PathVariable(value = "size") Integer size) {
        return service.findPage(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(20));
    }

    @PostMapping("/api/v1/book/")
    public void create(BooksRequest request) {
        service.create(request);
    }

    @PutMapping("/api/v1/book/{id}")
    public void update(
            @PathVariable("id") Long id,
            BooksRequest request) {
        service.update(id, request);
    }

    @DeleteMapping("/api/v1/book/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
