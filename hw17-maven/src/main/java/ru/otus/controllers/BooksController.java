package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.services.BooksService;

@RestController
@RequiredArgsConstructor
public class BooksController {

    private final BooksService service;

    @GetMapping("/api/v1/book/{id}")
    public Mono<BooksResponse> findById(@PathVariable("id") String id) {
        return service.findById(id);
    }

    @GetMapping("/api/v1/book/")
    public Mono<Page<BooksResponse>> findPage(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size) {
        return service.findPage(Mono.just(PageRequest.of(page, size)));
    }

    @PostMapping("/api/v1/book/")
    public Mono<BooksResponse> create(Mono<BooksRequest> request) {
        return service.create(request);
    }

    @PutMapping("/api/v1/book/{id}")
    public Mono<BooksResponse> update(
            @PathVariable("id") String id,
            Mono<BooksRequest> request) {
        return service.update(id, request);
    }

    @DeleteMapping("/api/v1/book/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return service.delete(id);
    }
}
