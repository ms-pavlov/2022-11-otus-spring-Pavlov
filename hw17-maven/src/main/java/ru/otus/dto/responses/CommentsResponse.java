package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsResponse {
    private String id;
    private String comment;
    private BooksResponse book;

    @Override
    public String toString() {
        return "[" + id + "]: " + comment + "\n\r";
    }
}
