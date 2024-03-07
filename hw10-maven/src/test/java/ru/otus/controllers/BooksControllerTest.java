package ru.otus.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.dto.requests.BooksRequest;
import ru.otus.dto.responses.AuthorsShortResponse;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.dto.responses.CommentsResponse;
import ru.otus.dto.responses.GenresShortResponse;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Comment;
import ru.otus.entities.Genre;
import ru.otus.services.BooksService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BooksController.class)
class BooksControllerTest {

    private static final Long BOOKS_ID = 1L;
    private static final Integer PAGE = 1;
    private static final Integer SIZE = 1;
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
    private final static Page<BooksResponse> BOOKS_RESPONSE_PAGE = Page.empty();

    @MockBean
    private BooksService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Создание")
    void create() throws Exception {
        Mockito.when(service.create(ArgumentMatchers.eq(BOOKS_REQUEST))).thenReturn(BOOKS_RESPONSE);

        mockMvc.perform(
                        post("/api/v1/book/")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .param("name", BOOKS_REQUEST.getName())
                                .param("authors", BOOKS_REQUEST.getAuthors().get(0))
                                .param("genres", BOOKS_REQUEST.getGenres().get(0)))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).create(ArgumentMatchers.eq(BOOKS_REQUEST));
    }

    @Test
    @DisplayName("Получение книги по id с комментариями")
    void findById() throws Exception {
        Mockito.when(service.findById(ArgumentMatchers.eq(BOOKS_ID))).thenReturn(BOOKS_RESPONSE);

        mockMvc.perform(get("/api/v1/book/"+ BOOKS_ID ))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"books_name\"," +
                        "\"authors\":[{\"id\":1,\"name\":\"author\"}]," +
                        "\"genres\":[{\"id\":1,\"name\":\"genre\"}]," +
                        "\"comments\":[{\"id\":1,\"comment\":\"comment\"}]}"));
    }

    @Test
    @DisplayName("Постраничный вывод")
    void findPage() throws Exception {
        Mockito.when(service.findPage(PAGE, SIZE)).thenReturn(BOOKS_RESPONSE_PAGE);

        mockMvc.perform(
                        get("/api/v1/book/"+ PAGE +"/"+ SIZE))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[]," +
                        "\"pageable\":\"INSTANCE\"," +
                        "\"last\":true," +
                        "\"totalElements\":0," +
                        "\"totalPages\":1," +
                        "\"size\":0," +
                        "\"number\":0," +
                        "\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true}," +
                        "\"numberOfElements\":0,\"first\":true,\"empty\":true}"));
    }

    @Test
    @DisplayName("Редактирование")
    void update() throws Exception {
        mockMvc.perform(
                put("/api/v1/book/" + BOOKS_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("name", BOOKS_REQUEST.getName())
                        .param("authors", BOOKS_REQUEST.getAuthors().get(0))
                        .param("genres", BOOKS_REQUEST.getGenres().get(0)))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).update(ArgumentMatchers.eq(BOOKS_ID), ArgumentMatchers.eq(BOOKS_REQUEST));
    }

    @Test
    @DisplayName("Удаление")
    void delete() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/book/" + BOOKS_ID))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).delete(BOOKS_ID);
    }
}