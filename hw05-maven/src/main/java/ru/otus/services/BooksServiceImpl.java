package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.dao.BooksDao;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.mappers.BookRequestMapper;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class BooksServiceImpl implements BooksService {
    private final static Logger log = Logger.getLogger(BooksServiceImpl.class.getName());
    private final BooksDao booksDao;
    private final BookRequestMapper mapper;

    @Autowired
    public BooksServiceImpl(BooksDao booksDao, BookRequestMapper mapper) {
        this.booksDao = booksDao;
        this.mapper = mapper;
    }

    @Override
    public BooksResponse create(BooksRequest request) {
        return Optional.ofNullable(request)
                .map(mapper::create)
                .map(booksDao::create)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public BooksResponse findById(Long id) {
        return booksDao.getById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public BooksResponse update(Long id, BooksRequest request) {
        return booksDao.getById(id)
                .map(books -> {
                    mapper.update(books, request);
                    return books;
                })
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        booksDao.delete(id);
    }

    @Override
    public List<BooksResponse> findByName(String name) {
        return booksDao.getByName(name)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BooksResponse> findByAuthor(String name) {
        return booksDao.getByAuthor(name)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BooksResponse> findByGenre(String name) {
        return booksDao.getByGenre(name)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BooksResponse> findAll() {
        return booksDao.getAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
