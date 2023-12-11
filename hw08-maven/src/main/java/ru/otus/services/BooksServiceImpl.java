package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class BooksServiceImpl implements BooksService {
    private final BooksRepository booksRepository;
    private final BookRequestMapper mapper;

    @Autowired
    public BooksServiceImpl(BooksRepository booksRepository, BookRequestMapper mapper) {
        this.booksRepository = booksRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public BooksResponse create(BooksRequest request) {
        return Optional.ofNullable(request)
                .map(mapper::create)
                .map(booksRepository::save)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public BooksResponse findById(String id) {
        return booksRepository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public BooksResponse update(String id, BooksRequest request) {
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
    public void delete(String id) {
        booksRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BooksResponse> findByName(String name) {
        return booksRepository.findByName(name)
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<BooksResponse> findAll() {
        return Optional.ofNullable(booksRepository)
                .map(BooksRepository::findAll)
                .map(Iterable::spliterator)
                .map(bookSpliterator -> StreamSupport.stream(bookSpliterator, false))
                .orElseGet(Stream::empty)
                .map(mapper::toDto)
                .toList();
    }
}