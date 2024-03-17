package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.services.AuthorsService;
import ru.otus.services.BooksService;
import ru.otus.services.GenresService;

@Controller
@RequiredArgsConstructor
public class FilterController {

    private final BooksService booksService;
    private final AuthorsService authorsService;
    private final GenresService genresService;

    @GetMapping("/filter")
    public String findByName(String name, Model model) {
        model.addAttribute("books", booksService.findByName(name));
        model.addAttribute("authors", authorsService.findByName(name));
        model.addAttribute("genres", genresService.findByName(name));
        return "filter";
    }
}
