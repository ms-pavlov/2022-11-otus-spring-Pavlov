package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BooksServiceImpl implements BooksService {
    private final BooksRepository booksRepository;
    private final BookRequestMapper mapper;

    @Autowired
    public BooksServiceImpl(BooksRepository booksRepository, BookRequestMapper mapper) {
        this.booksRepository = booksRepository;
        this.mapper = mapper;
    }

    @Override// save - открывает транзакцию по-умолчанию
    public BooksResponse create(BooksRequest request) {
        return Optional.ofNullable(request)
                .map(mapper::create)
                .map(booksRepository::save)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true) //findById без транзакции
    public BooksResponse findById(Long id) {
        return booksRepository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional //findById без транзакции, save - открывает транзакцию по-умолчанию
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

    @Override // deleteById - открывает транзакцию по-умолчанию
    public void delete(Long id) {
        booksRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true) //findByName - без транзакции
    public List<BooksResponse> findByName(String name) {
        return booksRepository.findByName(name)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true) //findAll - без транзакции
    public List<BooksResponse> findAll() {
        return booksRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    @Override
    @Transactional(readOnly = true) //findAll - без транзакции
    public Page<BooksResponse> findPage(int page, int size) {
        return booksRepository.findAll(PageRequest.of(page, size))
                .map(mapper::toDto);
    }
}
