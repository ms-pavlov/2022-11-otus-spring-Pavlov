package ru.otus.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.dto.responses.AuthorsResponse;
import ru.otus.services.AuthorsService;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorsService service;

    @GetMapping("/api/v1/author/{id}")
    public Mono<AuthorsResponse> findById(@PathVariable("id") Mono<String> id) {
        return service.findById(id);
    }

}
