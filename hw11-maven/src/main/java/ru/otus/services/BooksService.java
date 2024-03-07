package ru.otus.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;

public interface BooksService {

    Mono<BooksResponse> create(Mono<BooksRequest> request);

    Mono<BooksResponse> findById(String id);

    Mono<BooksResponse> update(String id, Mono<BooksRequest> request);

    Mono<Void> delete(String id);

    Mono<Page<BooksResponse>> findPage(Mono<Pageable> pageable);
}
