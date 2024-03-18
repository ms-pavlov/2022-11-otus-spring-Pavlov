package ru.otus.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.services.CommentsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentsController.class)
class CommentsControllerTest {

    private final static String TEST_COMMENT = "TEST_COMMENT";
    private final static Long TEST_BOOK_ID = 1L;

    @MockBean
    private CommentsService service;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    @DisplayName("Добавление комментария")
    void create() throws Exception {
        mockMvc.perform(
                        post("/comment")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .param("bookId", TEST_BOOK_ID.toString())
                                .param("comment", TEST_COMMENT)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(redirectedUrl(String.format("/book/%d/comment", TEST_BOOK_ID)));

        Mockito.verify(service, Mockito.times(1)).create(Mockito.any(), Mockito.any());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    @DisplayName("Удаление комментария")
    void delete() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/comment/" + TEST_BOOK_ID)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(forwardedUrl("/"));

        Mockito.verify(service, Mockito.times(1)).delete(TEST_BOOK_ID);
    }

    @Test
    @DisplayName("/comment/** закрыто авторизацией")
    void withoutAuth() throws Exception {
        mockMvc.perform(
                        post("/comment")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .param("bookId", TEST_BOOK_ID.toString())
                                .param("comment", TEST_COMMENT)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/comment/" + TEST_BOOK_ID)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isUnauthorized());
    }
}