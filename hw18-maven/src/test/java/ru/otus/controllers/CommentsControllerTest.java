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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

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
                        post("/comment")
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                .param("bookId", TEST_BOOK_ID.toString())
                                .param("comment", TEST_COMMENT))
                .andExpect(redirectedUrl(String.format("/book/%d/comment", TEST_BOOK_ID)));

        Mockito.verify(service, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    @DisplayName("Удаление комментария")
    void delete() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/comment/" + TEST_BOOK_ID))
                .andExpect(forwardedUrl("/"));

        Mockito.verify(service, Mockito.times(1)).delete(TEST_BOOK_ID);
    }
}