package ru.otus.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.dto.responses.AuthorsResponse;
import ru.otus.dto.responses.BooksResponse;
import ru.otus.dto.responses.GenresResponse;
import ru.otus.services.AuthorsService;
import ru.otus.services.BooksService;
import ru.otus.services.GenresService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilterController.class)
class FilterControllerTest {

    private final static String NAME = "NAME";
    private final static List<BooksResponse> BOOKS = List.of();
    private final static AuthorsResponse AUTHOR = new AuthorsResponse(null, null, null);
    private final static GenresResponse GENRES = new GenresResponse(null, null, null);

    @MockBean
    private BooksService booksService;
    @MockBean
    private AuthorsService authorsService;
    @MockBean
    private GenresService genresService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Поиск")
    void findByName() throws Exception {
        Mockito.when(booksService.findByName(NAME)).thenReturn(BOOKS);
        Mockito.when(authorsService.findByName(NAME)).thenReturn(AUTHOR);
        Mockito.when(genresService.findByName(NAME)).thenReturn(GENRES);

        mockMvc.perform(
                        get("/filter")
                                .param("name", NAME))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", BOOKS))
                .andExpect(model().attribute("authors", AUTHOR))
                .andExpect(model().attribute("genres", GENRES));
    }
}