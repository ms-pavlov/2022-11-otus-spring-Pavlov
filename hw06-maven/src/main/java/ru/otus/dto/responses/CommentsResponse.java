package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentsResponse {
    private final Long id;
    private final String comment;
    private final BooksResponse book;
}
