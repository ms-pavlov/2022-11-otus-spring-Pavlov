package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.services.CommentsService;

@Controller
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService service;

    @PostMapping("/comment")
    public String create(CommentsRequest request) {
        service.create(request);
        return String.format("redirect:/book/%d/comment", request.getBookId());
    }

    @DeleteMapping("/comment/{id}")
    public String delete(@PathVariable("id") Long id) {
        service.delete(id);
        return "forward:/";
    }


}
