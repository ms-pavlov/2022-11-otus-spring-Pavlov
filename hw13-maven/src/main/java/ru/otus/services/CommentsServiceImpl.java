package ru.otus.services;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.mappers.CommentsMapper;
import ru.otus.repositories.CommentsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @PostFilter("filterObject.ownerLogin == authentication.name")
    public List<CommentsResponse> getAll() {
        return commentsRepository.findAll()
                .stream()
                .map(commentsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentsResponse create(
            CommentsRequest request,
            Authentication authentication) {
        return Optional.of(request)
                .map(value -> commentsMapper.create(value, authentication))
                .map(commentsRepository::save)
                .map(commentsMapper::toDto)
                .orElse(null);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') || @commentsRepository.getOwnerById(#id) == authentication.name")
    public void delete(Long id) {
        commentsRepository.deleteById(id);
    }

}
