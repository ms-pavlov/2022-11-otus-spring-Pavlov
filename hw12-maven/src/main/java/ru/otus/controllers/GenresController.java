package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.dto.responses.GenresResponse;
import ru.otus.services.GenresService;

@Controller
@RequiredArgsConstructor
public class GenresController {

    private final GenresService service;

    @GetMapping("/genre/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        GenresResponse genre = service.findById(id);
        model.addAttribute("genre", genre);
        model.addAttribute("books", genre.getBooks());
        return "genres/index";
    }
}
