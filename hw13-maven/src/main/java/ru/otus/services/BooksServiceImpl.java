package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
                .map(mapper::toFullDto)
                .orElse(null);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void delete(Long id) {
        commentsRepository.deleteByBookId(id);
        booksRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true) //findByName - без транзакции
    public List<BooksResponse> findByName(String name) {
        return booksRepository.findByName("%" + name + "%")
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
    public Page<BooksResponse> findPage(Integer page, Integer size) {
        return booksRepository.findAll(PageRequest.of(page, size))
                .map(mapper::toDto);
    }
}
