package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.repositories.BooksRepository;
import ru.otus.repositories.CommentsRepository;

@Service
public class BooksServiceImpl implements BooksService {
    private final BooksRepository booksRepository;
    private final CommentsRepository commentsRepository;
    private final BookRequestMapper mapper;

    @Autowired
    public BooksServiceImpl(
            BooksRepository booksRepository,
            CommentsRepository commentsRepository,
            BookRequestMapper mapper) {
        this.booksRepository = booksRepository;
        this.commentsRepository = commentsRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<BooksResponse> create(Mono<BooksRequest> request) {
        return request
                .map(mapper::create)
                .flatMap(booksRepository::save)
                .map(mapper::toDto);
    }

    @Override
    public Mono<BooksResponse> findById(String id) {
        return booksRepository.findById(id)
                .map(mapper::toDto);
    }

    @Override
    public Mono<BooksResponse> update(String id, Mono<BooksRequest> request) {
        return booksRepository.findById(id)
                .flatMap(books -> request.map(
                        value -> {
                            mapper.update(books, value);
                            return books;
                        }))
                .flatMap(booksRepository::save)
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return commentsRepository.deleteByBookId(id)
                .then(booksRepository.deleteById(id));
    }


    @Override
    public Mono<Page<BooksResponse>> findPage(Mono<Pageable> pageable) {
        return pageable
                .flatMap(
                        value -> booksRepository.findAllBy(value)
                                .map(mapper::toDto)
                                .collectList()
                                .zipWith(booksRepository.count())
                                .map(p -> new PageImpl<>(p.getT1(), value, p.getT2())));
    }
}
