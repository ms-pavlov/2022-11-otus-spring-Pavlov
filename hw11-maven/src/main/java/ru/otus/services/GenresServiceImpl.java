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
public class GenresServiceImpl implements GenresService {

    private final BooksRepository repository;
    private final BookRequestMapper mapper;

    @Override
    public Flux<BooksResponse> findByGenre(Mono<String> name) {
        return name.flatMapMany(repository::findAllByGenresContains)
                .map(mapper::toDto);
    }

}
