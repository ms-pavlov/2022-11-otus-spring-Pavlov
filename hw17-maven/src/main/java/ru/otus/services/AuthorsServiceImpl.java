package ru.otus.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.repositories.BooksRepository;

@Service
@AllArgsConstructor
public class AuthorsServiceImpl implements AuthorsService {

    private final BooksRepository repository;
    private final BookRequestMapper mapper;


    @Override
    public Flux<BooksResponse> findByAuthor(Mono<String> name) {
        return name.flatMapMany(repository::findAllByAuthorsContains)
                .map(mapper::toDto);
    }
}
