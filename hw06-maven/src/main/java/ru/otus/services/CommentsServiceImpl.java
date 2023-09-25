package ru.otus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.mappers.CommentsMapper;
import ru.otus.repositories.CommentsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommentsServiceImpl implements CommentsService{

    private final CommentsRepository repository;
    private final CommentsMapper mapper;

    public CommentsServiceImpl(CommentsRepository repository, CommentsMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    @Transactional
    public CommentsResponse create(CommentsRequest request) {
        return Optional.of(request)
                .map(mapper::create)
                .map(repository::create)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentsResponse findById(Long id) {
        return repository.getById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public CommentsResponse update(Long id, CommentsRequest request) {
        return repository.getById(id)
                .map(author -> {
                    mapper.update(author, request);
                    return author;
                })
                .map(repository::update)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentsResponse> findByBookId(Long bookId) {
        return repository.getCommentsByBookId(bookId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentsResponse> findAll() {
        return repository.getAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
