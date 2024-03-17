package ru.otus.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.services.AuthorsService;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorsService service;

    @GetMapping("/api/v1/author")
    public Flux<BooksResponse> findById(@RequestParam("name") String name) {
        return service.findByAuthor(Mono.just(name));
    }

}
