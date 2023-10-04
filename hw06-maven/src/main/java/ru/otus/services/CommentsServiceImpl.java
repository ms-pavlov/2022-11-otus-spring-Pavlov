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
                .map(commentsRepository::create)
                .map(commentsMapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentsResponse findById(Long id) {
        return commentsRepository.getById(id)
                .map(commentsMapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public CommentsResponse update(Long id, CommentsRequest request) {
        return commentsRepository.getById(id)
                .map(author -> {
                    commentsMapper.update(author, request);
                    return author;
                })
                .map(commentsRepository::update)
                .map(commentsMapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        commentsRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BookWithCommentsResponse findByBookId(Long bookId) {
        return booksRepository.getById(bookId)
                        .map(book -> new BookWithCommentsResponse(
                                bookMapper.toDto(book),
                                book.getComments().stream()
                                        .map(commentsMapper::toDto)
                                        .toList()
                        ))
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentsResponse> findAll() {
        return commentsRepository.getAll()
                .stream()
                .map(commentsMapper::toDto)
                .toList();
    }
}
