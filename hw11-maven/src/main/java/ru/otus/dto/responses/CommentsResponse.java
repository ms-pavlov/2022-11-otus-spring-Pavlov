package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentsResponse {
    private final String id;
    private final String comment;
    private final BooksResponse book;

    @Override
    public String toString() {
        return "[" + id + "]: " + comment + "\n\r";
    }
}
