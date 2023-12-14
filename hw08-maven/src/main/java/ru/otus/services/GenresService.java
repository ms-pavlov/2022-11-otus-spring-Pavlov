package ru.otus.services;

import ru.otus.dto.requests.GenresRequest;
import ru.otus.dto.responses.GenresResponse;

import java.util.List;

public interface GenresService {

    GenresResponse create(GenresRequest request);

    GenresResponse findById(String id);

    GenresResponse update(String id, GenresRequest request);

    void delete(String id);

    GenresResponse findByName(String name);

    List<GenresResponse> findAll();
}
