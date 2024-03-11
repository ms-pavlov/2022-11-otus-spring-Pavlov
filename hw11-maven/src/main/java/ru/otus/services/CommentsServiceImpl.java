package ru.otus.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.mappers.CommentsMapper;
import ru.otus.repositories.CommentsRepository;

@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final CommentsMapper commentsMapper;

    public CommentsServiceImpl(
            CommentsRepository repository,
            CommentsMapper mapper) {
        this.commentsRepository = repository;
        this.commentsMapper = mapper;
    }

    @Override
    public Flux<CommentsResponse> getCommentsByBook(Mono<String> id) {
        return id.flatMapMany(commentsRepository::findByBookId)
                .map(commentsMapper::toDto);
    }

    @Override
    public Mono<CommentsResponse> create(Mono<CommentsRequest> request) {
        return request
                .map(commentsMapper::create)
                .flatMap(commentsRepository::save)
                .map(commentsMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Mono<String> id) {
        return commentsRepository.deleteById(id);
    }

}
