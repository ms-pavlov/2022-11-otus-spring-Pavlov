package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.otus.dto.requests.GenresRequest;
import ru.otus.services.GenresService;

@Controller
@RequiredArgsConstructor
public class GenresController {

    private final GenresService service;

    public void create(GenresRequest request, Model model) {
        service.create(request);
    }

    public void findById(Long id, Model model) {
        service.findById(id);
    }


    public void findByName(String name, Model model) {
        service.findByName(name);
    }

    public void update(Long id, GenresRequest request, Model model) {
        service.update(id, request);
    }

    public void delete(Long id, Model model) {
        service.delete(id);
    }

    public void findAll(Model model) {
        service.findAll();
    }
}
