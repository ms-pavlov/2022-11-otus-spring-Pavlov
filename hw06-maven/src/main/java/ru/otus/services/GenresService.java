package ru.otus.services;

import ru.otus.dto.requests.GenresRequest;
import ru.otus.dto.responses.GenresResponse;

import java.util.List;

public interface GenresService {

    GenresResponse create(GenresRequest request);

    GenresResponse findById(Long id);

    GenresResponse update(Long id, GenresRequest request);

    void delete(Long id);

    List<GenresResponse> findByName(String name);

    List<GenresResponse> findAll();
}
