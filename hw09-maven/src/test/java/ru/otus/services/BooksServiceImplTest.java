package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.AuthorsShortResponse;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.dto.responses.GenresShortResponse;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Comment;
import ru.otus.entities.Genre;
import ru.otus.mappers.BookRequestMapper;
import ru.otus.repositories.BooksRepository;
import ru.otus.repositories.CommentsRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Service для работы с книгами должен")
@SpringBootTest(classes = {
        BooksServiceImpl.class
})
class BooksServiceImplTest {
    private static final Long BOOKS_ID = 1L;
    private static final String BOOKS_NAME = "books_name";
    private final static Book BOOK = new Book(
            BOOKS_ID,
            BOOKS_NAME,
            List.of(new Author(1L, "author")),
            List.of(new Genre(1L, "genre")),
            List.of(new Comment(1L, "comment")));
    private final static BooksResponse BOOKS_RESPONSE = new BooksResponse(
            BOOK.getId(),
            BOOK.getName(),
            BOOK.getAuthors()
                    .stream()
                    .map(author -> new AuthorsShortResponse(author.getId(), author.getName()))
                    .toList(),
            BOOK.getGenres()
                    .stream()
                    .map(genre -> new GenresShortResponse(genre.getId(), genre.getName()))
                    .toList(),
            BOOK.getComments()
                    .stream()
                    .map(comment -> new CommentsResponse(comment.getId(), comment.getComment()))
                    .toList());
    private final static BooksRequest BOOKS_REQUEST = new BooksRequest(BOOK.getName(), List.of("author"), List.of("genre"));

    @MockBean
    private BooksRepository booksRepository;
    @MockBean
    private CommentsRepository commentsRepository;
    @MockBean
    private BookRequestMapper mapper;
    @Autowired
    private BooksService service;

    @Test
    @DisplayName("должен смапить запрос в новую книгу, сохранить и вернуть результат")
    void create() {
        when(mapper.create(BOOKS_REQUEST)).thenReturn(BOOK);
        when(booksRepository.save(BOOK))
                .thenReturn(BOOK);
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.create(BOOKS_REQUEST);

        assertEquals(BOOKS_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти книгу по id и вернуть результат")
    void findById() {
        when(booksRepository.findById(BOOKS_ID)).thenReturn(Optional.of(BOOK));
        when(mapper.toFullDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findById(BOOKS_ID);

        assertEquals(BOOKS_RESPONSE, result);
    }

    @Test
    @DisplayName("должен найти книгу по id, установить новые значения из запроса, обновить и вернуть результат")
    void update() {
        when(booksRepository.findById(BOOKS_ID))
                .thenReturn(Optional.of(BOOK));
        doNothing().when(mapper).update(BOOK, BOOKS_REQUEST);
        when(booksRepository.save(BOOK))
                .thenReturn(BOOK);
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.update(BOOKS_ID, BOOKS_REQUEST);

        assertEquals(BOOKS_RESPONSE, result);
        verify(mapper, times(1)).update(BOOK, BOOKS_REQUEST);
        verify(booksRepository, times(1)).save(BOOK);
    }

    @Test
    @DisplayName("должен удалить книгу по id")
    void delete() {
        service.delete(BOOKS_ID);

        verify(booksRepository, times(1)).deleteById(BOOKS_ID);
        verify(commentsRepository, times(1)).deleteByBookId(BOOKS_ID);
    }

    @Test
    @DisplayName("должен найти книгу по названию")
    void findByName() {
        when(booksRepository.findByName(BOOKS_NAME)).thenReturn(List.of(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findByName(BOOKS_NAME);

        result.forEach(item -> assertEquals(BOOKS_RESPONSE, item));

    }

    @Test
    @DisplayName("должен найти все книги")
    void findAll() {
        when(booksRepository.findAll()).thenReturn(List.of(BOOK));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findAll();

        result.forEach(item -> assertEquals(BOOKS_RESPONSE, item));
    }

    @Test
    @DisplayName("должен найти постранично книги")
    void findPage() {
        when(booksRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(BOOK)));
        when(mapper.toDto(BOOK))
                .thenReturn(BOOKS_RESPONSE);

        var result = service.findPage(0, 1);

        result.forEach(item -> assertEquals(BOOKS_RESPONSE, item));
    }
}