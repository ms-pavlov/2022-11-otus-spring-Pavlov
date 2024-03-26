package ru.otus.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.dto.responses.GenresResponse;
import ru.otus.services.GenresService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenresController.class)
class GenresControllerTest {
    private static final Long GENRES_ID = 1L;
    private static final String GENRES_NAME = "name";
    private final static GenresResponse GENRES_RESPONSE = new GenresResponse(
            GENRES_ID,
            GENRES_NAME,
            List.of());

    @MockBean
    private GenresService service;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(
            username = "admin",
            authorities = {"ADMIN"}
    )
    @Test
    @DisplayName("Поиск жанра по id")
    void findById() throws Exception {
        Mockito.when(service.findById(GENRES_ID)).thenReturn(GENRES_RESPONSE);

        mockMvc.perform(get("/genre/" + GENRES_ID))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genre", GENRES_RESPONSE))
                .andExpect(model().attribute("books", GENRES_RESPONSE.getBooks()));
    }

    @Test
    @DisplayName("/genre/** закрыто авторизацией")
    void withoutAuth() throws Exception {
        mockMvc.perform(get("/genre/" + GENRES_ID))
                .andExpect(status().isUnauthorized());
    }
}