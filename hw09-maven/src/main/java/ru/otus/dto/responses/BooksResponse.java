package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BooksResponse {
    private final Long id;
    private final String name;
    private final List<AuthorsShortResponse> authors;
    private final List<GenresShortResponse> genres;
    private final List<CommentsResponse> comments;
}
