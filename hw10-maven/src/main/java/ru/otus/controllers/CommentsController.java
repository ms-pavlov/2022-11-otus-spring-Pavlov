package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.services.CommentsService;

@RestController
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService service;

    @PostMapping("/api/v1/comment")
    public void create(CommentsRequest request) {
        service.create(request);
    }

    @DeleteMapping("/api/v1/comment/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }


}
