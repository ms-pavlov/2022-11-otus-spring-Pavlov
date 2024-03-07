package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.services.CommentsService;

@RestController
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService service;

    @PostMapping("/api/v1/comment")
    public Mono<Void> create(Mono<CommentsRequest> request) {
        return service.create(request).then();
    }

    @DeleteMapping("/api/v1/comment/{id}")
    public Mono<Void> delete(@PathVariable("id") Mono<String> id) {
        return service.delete(id);
    }


}
