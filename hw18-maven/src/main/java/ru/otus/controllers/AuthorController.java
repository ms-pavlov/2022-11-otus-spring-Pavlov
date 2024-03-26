package ru.otus.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.dto.responses.AuthorsResponse;
import ru.otus.services.AuthorsService;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorsService service;

    @GetMapping("/author/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        AuthorsResponse author = service.findById(id);
        model.addAttribute("author", author);
        model.addAttribute("books", author.getBooks());
        return "authors/index";
    }

}
