package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.dto.requests.CommentsRequest;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.entities.Book;
import ru.otus.entities.Comment;
import ru.otus.mappers.CommentsMapper;
import ru.otus.repositories.CommentsRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Service для работы с жанрами должен")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class CommentsServiceImplTest {

    private final static Long TEST_COMMENT_ID = 1L;
    private final static Long TEST_BOOK_ID = 1L;
    private final static String TEST_COMMENT = "TEST_COMMENT";
    private final static String TEST_BOOK_NAME = "books_name";
    private final static String USER_NAME = "USER_NAME";
    private final static CommentsRequest TEST_COMMENT_REQUEST = new CommentsRequest(TEST_COMMENT, TEST_BOOK_ID);
    private final static CommentsResponse COMMENT_RESPONSE = new CommentsResponse(
            TEST_COMMENT_ID,
            TEST_COMMENT,
            null,
            USER_NAME);
    private final static CommentsResponse COMMENT_RESPONSE2 = new CommentsResponse(
            TEST_COMMENT_ID,
            TEST_COMMENT,
            null,
            USER_NAME + "FAIL");
    private final static Book TEST_BOOK = new Book(
            TEST_BOOK_ID,
            TEST_BOOK_NAME,
            List.of(),
            List.of(),
            List.of(new Comment(TEST_COMMENT_ID, TEST_COMMENT, null, null)));
    private final static Comment TEST_COMMENT_OBJECT = new Comment(
            TEST_COMMENT_ID, TEST_COMMENT, TEST_BOOK, null);
    private final static Comment TEST_COMMENT_OBJECT2 = new Comment(
            TEST_COMMENT_ID, TEST_COMMENT, TEST_BOOK, null);

    @MockBean
    private CommentsRepository commentsRepository;
    @MockBean
    private CommentsMapper commentsMapper;
    @MockBean
    private UsersService usersService;
    @Mock
    private Authentication authentication;

    @Autowired
    private CommentsService service;

    @Test
    @DisplayName("создает комментарий и возвращает результат")
    void create() {
        when(commentsMapper.create(TEST_COMMENT_REQUEST, authentication)).thenReturn(TEST_COMMENT_OBJECT);
        when(commentsRepository.save(TEST_COMMENT_OBJECT))
                .thenReturn(TEST_COMMENT_OBJECT);
        when(commentsMapper.toDto(TEST_COMMENT_OBJECT))
                .thenReturn(COMMENT_RESPONSE);

        var result = service.create(TEST_COMMENT_REQUEST, authentication);

        assertEquals(COMMENT_RESPONSE, result);
        verify(commentsRepository, times(1)).save(TEST_COMMENT_OBJECT);
        verify(commentsMapper, times(1)).create(TEST_COMMENT_REQUEST, authentication);
        verify(commentsMapper, times(1)).toDto(TEST_COMMENT_OBJECT);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("удаление не возможно для анонимного пользователя")
    void deleteFail() {
        assertThrows(AccessDeniedException.class, () -> service.delete(TEST_COMMENT_ID));
    }

    @Test
    @WithMockUser(
            roles = "ADMIN"
    )
    @DisplayName("должен удалить комментарий по id если пользователь с ролью ADMIN")
    void deleteByAdmin() {
        service.delete(TEST_COMMENT_ID);

        verify(commentsRepository, times(1)).deleteById(TEST_COMMENT_ID);
    }

    @Test
    @WithMockUser(
            username = USER_NAME
    )
    @DisplayName("должен удалить комментарий по id если пользователь владелец")
    void deleteByOwner() {
        when(commentsRepository.getOwnerById(TEST_COMMENT_ID)).thenReturn(USER_NAME);

        service.delete(TEST_COMMENT_ID);

        verify(commentsRepository, times(1)).deleteById(TEST_COMMENT_ID);
    }

    @Test
    @WithMockUser(
            username = USER_NAME
    )
    @DisplayName("возвращает только комментарии принадлежащие пользователю")
    void getAll() {
        when(commentsRepository.findAll()).thenReturn(
                List.of(
                        TEST_COMMENT_OBJECT,
                        TEST_COMMENT_OBJECT2)
        );
        when(commentsMapper.toDto(TEST_COMMENT_OBJECT))
                .thenReturn(COMMENT_RESPONSE);
        when(commentsMapper.toDto(TEST_COMMENT_OBJECT2))
                .thenReturn(COMMENT_RESPONSE2);

        var result = service.getAll();

        assertTrue(result.stream().allMatch(commentsResponse -> USER_NAME.equals(commentsResponse.getOwnerLogin())));
    }
}