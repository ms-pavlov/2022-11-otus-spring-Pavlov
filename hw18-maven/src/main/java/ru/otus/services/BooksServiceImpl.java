package ru.otus.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.repositories.BooksRepository;
import ru.otus.repositories.CommentsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BooksServiceImpl implements BooksService {
    private final BooksRepository booksRepository;
    private final CommentsRepository commentsRepository;
    private final BookRequestMapper mapper;

    @Autowired
    public BooksServiceImpl(BooksRepository booksRepository, CommentsRepository commentsRepository, BookRequestMapper mapper) {
        this.booksRepository = booksRepository;
        this.commentsRepository = commentsRepository;
        this.mapper = mapper;
    }

    @Override
    @Retry(name = "bookRetry")
    public BooksResponse create(BooksRequest request) {
        return Optional.ofNullable(request)
                .map(mapper::create)
                .map(booksRepository::save)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "bookCircuitBreaker", fallbackMethod = "emptyBook")
    public BooksResponse findById(Long id) {
        return booksRepository.findById(id)
                .map(mapper::toFullDto)
                .orElse(null);
    }

    @Override
    @Transactional
    @Retry(name = "bookRetry")
    public BooksResponse update(Long id, BooksRequest request) {
        return booksRepository.findById(id)
                .map(books -> {
                    mapper.update(books, request);
                    return books;
                })
                .map(booksRepository::save)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    @Retry(name = "bookRetry")
    public void delete(Long id) {
        commentsRepository.deleteByBookId(id);
        booksRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "bookCircuitBreaker", fallbackMethod = "emptyBookList")
    public List<BooksResponse> findByName(String name) {
        return booksRepository.findByName("%" + name + "%")
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "bookCircuitBreaker", fallbackMethod = "emptyBookList")
    public List<BooksResponse> findAll() {
        return booksRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    @RateLimiter(name = "bookRateLimiter", fallbackMethod = "emptyBookPage")
    @CircuitBreaker(name = "bookCircuitBreaker", fallbackMethod = "emptyBookPage")
    public Page<BooksResponse> findPage(Integer page, Integer size) {
        return booksRepository.findAll(PageRequest.of(page, size))
                .map(mapper::toDto);
    }

    public BooksResponse emptyBook(Exception ex) {
        return new BooksResponse(null, "N/A", List.of(), List.of(), List.of());
    }

    public List<BooksResponse> emptyBookList(Exception ex) {
        return List.of();
    }

    public Page<BooksResponse> emptyBookPage(Exception ex) {
        System.out.println("emptyBookPage Test");
        return Page.empty();
    }
}
