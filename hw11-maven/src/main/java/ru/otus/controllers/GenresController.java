package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.dto.responses.GenresResponse;
import ru.otus.services.GenresService;

@RestController
@RequiredArgsConstructor
public class GenresController {

    private final GenresService service;

    @GetMapping("/api/v1/genre/{id}")
    public Mono<GenresResponse> findById(@PathVariable("id") Mono<String> id) {
        return service.findById(id);
    }
}
