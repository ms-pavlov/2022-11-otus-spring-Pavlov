package ru.otus.services;

import ru.otus.dto.requests.AuthorsRequest;
import ru.otus.dto.responses.AuthorsResponse;

import java.util.List;

public interface AuthorsService {

    AuthorsResponse create(AuthorsRequest request);

    AuthorsResponse findById(Long id);

    AuthorsResponse update(Long id, AuthorsRequest request);

    void delete(Long id);

    List<AuthorsResponse> findByName(String name);

    List<AuthorsResponse> findAll();
}
