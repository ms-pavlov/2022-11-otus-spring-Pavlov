package ru.otus.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.config.SecurityConfig;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
@Import(SecurityConfig.class)
class MainControllerTest {

    private static final Long BOOKS_ID = 1L;
    private static final Long AUTHOR_ID = 1L;
    private static final Long GENRE_ID = 1L;
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
                    .map(comment -> new CommentsResponse(comment.getId(), comment.getComment(), null, null))
                    .toList());

    @MockBean
    private BooksService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Стартовая страница")
    void index() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Страница для отображения автора")
    void getAuthor() throws Exception {
        mockMvc.perform(get("/author/" + AUTHOR_ID))
                .andExpect(status().isOk())
                .andExpect(model().attribute("id", AUTHOR_ID));
    }

    @Test
    @DisplayName("Страница для отображения жанра")
    void getGenre() throws Exception {
        mockMvc.perform(get("/genre/" + GENRE_ID))
                .andExpect(status().isOk())
                .andExpect(model().attribute("id", GENRE_ID));
    }

    @Test
    @DisplayName("Страница для отображения книги")
    void getBook() throws Exception {
        mockMvc.perform(get("/book/" + BOOKS_ID + "/comment"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("id", BOOKS_ID));
    }

    @Test
    @DisplayName("Форма для создания книги")
    void createBook() throws Exception {
        mockMvc.perform(get("/book/form"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Форма для редактирования книги")
    void editBook() throws Exception {
        Mockito.when(service.findById(ArgumentMatchers.eq(BOOKS_ID))).thenReturn(BOOKS_RESPONSE);

        mockMvc.perform(get("/book/" + BOOKS_ID + "/form"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", BOOKS_RESPONSE));
    }
}