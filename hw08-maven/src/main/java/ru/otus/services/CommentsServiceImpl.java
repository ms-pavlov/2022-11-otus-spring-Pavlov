package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.BookWithCommentsResponse;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.entities.Comment;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.mappers.CommentsMapper;
import ru.otus.repositories.CommentsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final CommentsMapper commentsMapper;
    private final BookRequestMapper bookMapper;

    public CommentsServiceImpl(
            CommentsRepository repository,
            CommentsMapper mapper,
            BookRequestMapper bookMapper) {
        this.commentsRepository = repository;
        this.commentsMapper = mapper;
        this.bookMapper = bookMapper;
    }


    @Override
    public CommentsResponse create(CommentsRequest request) {
        return Optional.of(request)
                .map(commentsMapper::create)
                .map(commentsRepository::save)
                .map(commentsMapper::toDto)
                .orElse(null);
    }

    @Override
    public CommentsResponse findById(String id) {
        return commentsRepository.findById(id)
                .map(commentsMapper::toDto)
                .orElse(null);
    }

    @Override
    public CommentsResponse update(String id, CommentsRequest request) {
        return commentsRepository.findById(id)
                .map(author -> {
                    commentsMapper.update(author, request);
                    return author;
                })
                .map(commentsRepository::save)
                .map(commentsMapper::toDto)
                .orElse(null);
    }

    @Override
    public void delete(String id) {
        commentsRepository.deleteById(id);
    }

    @Override
    public BookWithCommentsResponse findByBookId(String bookId) {
        var comments = commentsRepository.findByBookId(bookId)
                .stream()
                .collect(Collectors.groupingBy(Comment::getBook));
        return comments.keySet()
                .stream()
                .findFirst()
                .map(
                        booksResponse -> new BookWithCommentsResponse(
                                bookMapper.toDto(booksResponse),
                                comments.get(booksResponse)
                                        .stream()
                                        .map(commentsMapper::toDto)
                                        .toList()
                        ))
                .orElseGet(BookWithCommentsResponse::new);
    }

    @Override
    public List<CommentsResponse> findAll() {
        return Optional.ofNullable(commentsRepository)
                .map(CommentsRepository::findAll)
                .map(Iterable::spliterator)
                .map(commentSpliterator -> StreamSupport.stream(commentSpliterator, false))
                .orElseGet(Stream::empty)
                .map(commentsMapper::toDto)
                .toList();
    }
}
