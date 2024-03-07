package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class BookWithCommentsResponse {
    private final BooksResponse book;
    private final List<CommentsResponse> comments;

    public BookWithCommentsResponse() {
        book = null;
        comments = new ArrayList<>();
    }

    public boolean isEmpty() {
        return comments.isEmpty();
    }
}
