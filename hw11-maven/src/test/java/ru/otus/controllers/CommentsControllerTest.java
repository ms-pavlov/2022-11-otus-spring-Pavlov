package ru.otus.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.services.CommentsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentsController.class)
class CommentsControllerTest {

    private final static String TEST_COMMENT = "TEST_COMMENT";
    private final static Long TEST_BOOK_ID = 1L;

    @MockBean
    private CommentsService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Добавление комментария")
    void create() throws Exception {
        mockMvc.perform(
                post("/api/v1/comment")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("bookId", TEST_BOOK_ID.toString())
                        .param("comment", TEST_COMMENT))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    @DisplayName("Удаление комментария")
    void delete() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/comment/" + TEST_BOOK_ID))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).delete(TEST_BOOK_ID);
    }
}