package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class BooksServiceImpl implements BooksService {
    private final static Logger log = Logger.getLogger(BooksServiceImpl.class.getName());
    private final BooksRepository booksRepository;
    private final BookRequestMapper mapper;

    @Autowired
    public BooksServiceImpl(BooksRepository booksRepository, BookRequestMapper mapper) {
        this.booksRepository = booksRepository;
        this.mapper = mapper;
    }

    @Override
    public BooksResponse create(BooksRequest request) {
        return Optional.ofNullable(request)
                .map(mapper::create)
                .map(booksRepository::create)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public BooksResponse findById(Long id) {
        return booksRepository.getById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public BooksResponse update(Long id, BooksRequest request) {
        return booksRepository.getById(id)
                .map(books -> {
                    mapper.update(books, request);
                    return books;
                })
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        booksRepository.delete(id);
    }

    @Override
    public List<BooksResponse> findByName(String name) {
        return booksRepository.getByName(name)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BooksResponse> findByAuthor(String name) {
        return booksRepository.getByAuthor(name)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BooksResponse> findByGenre(String name) {
        return booksRepository.getByGenre(name)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BooksResponse> findAll() {
        return booksRepository.getAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
