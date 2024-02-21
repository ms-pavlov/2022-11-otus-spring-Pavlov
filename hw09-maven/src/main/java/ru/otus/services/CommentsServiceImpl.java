package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.mappers.CommentsMapper;
import ru.otus.repositories.CommentsRepository;

import java.util.Optional;

@Service
public class CommentsServiceImpl implements CommentsService{

    private final CommentsRepository commentsRepository;
    private final CommentsMapper commentsMapper;

    public CommentsServiceImpl(
            CommentsRepository repository,
            CommentsMapper mapper) {
        this.commentsRepository = repository;
        this.commentsMapper = mapper;
    }


    @Override //save - открывает транзакцию по-умолчанию
    public CommentsResponse create(CommentsRequest request) {
        return Optional.of(request)
                .map(commentsMapper::create)
                .map(commentsRepository::save)
                .map(commentsMapper::toDto)
                .orElse(null);
    }

    @Override // deleteById - открывает транзакцию по-умолчанию
    public void delete(Long id) {
        commentsRepository.deleteById(id);
    }

}
