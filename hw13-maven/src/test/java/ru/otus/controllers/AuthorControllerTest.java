package ru.otus.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.config.SecurityConfig;
import ru.otus.dto.responses.AuthorsResponse;
import ru.otus.services.AuthorsService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@Import({SecurityConfig.class})
class AuthorControllerTest {

    private static final Long AUTHORS_ID = 1L;
    private static final String AUTHORS_NAME = "name";

    private final static AuthorsResponse AUTHORS_RESPONSE = new AuthorsResponse(
            AUTHORS_ID,
            AUTHORS_NAME,
            List.of());

    @MockBean
    private AuthorsService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Поиск автора по id")
    void findById() throws Exception {
        BDDMockito.given(service.findById(AUTHORS_ID))
                .willReturn(AUTHORS_RESPONSE);

        mockMvc.perform(get("/api/v1/author/" + AUTHORS_ID))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"name\",\"books\":[]}"));
    }
}