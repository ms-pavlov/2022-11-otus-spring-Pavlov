package ru.otus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.BookWithCommentsResponse;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.mappers.CommentsMapper;
import ru.otus.repositories.BooksRepository;
import ru.otus.repositories.CommentsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class CommentsServiceImpl implements CommentsService{

    private final CommentsRepository commentsRepository;
    private final BooksRepository booksRepository;
    private final CommentsMapper commentsMapper;
    private final BookRequestMapper bookMapper;

    public CommentsServiceImpl(
            CommentsRepository repository,
            BooksRepository booksRepository,
            CommentsMapper mapper,
            BookRequestMapper bookMapper) {
        this.commentsRepository = repository;
        this.booksRepository = booksRepository;
        this.commentsMapper = mapper;
        this.bookMapper = bookMapper;
    }


    @Override
    @Transactional
    public CommentsResponse create(CommentsRequest request) {
        return Optional.of(request)
                .map(commentsMapper::create)
                .map(commentsRepository::save)
                .map(commentsMapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentsResponse findById(String id) {
        return commentsRepository.findById(id)
                .map(commentsMapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
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
    @Transactional
    public void delete(String id) {
        commentsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BookWithCommentsResponse findByBookId(String bookId) {
        return booksRepository.findById(bookId)
                        .map(book -> new BookWithCommentsResponse(
                                bookMapper.toDto(book),
                                commentsRepository.findByBookId(bookId)
                                        .stream()
                                        .map(commentsMapper::toDto)
                                        .toList()
                        ))
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
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
