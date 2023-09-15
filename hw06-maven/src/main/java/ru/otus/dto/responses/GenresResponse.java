package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GenresResponse {
    private final Long id;
    private final String name;
    private final List<BooksResponse> books;

    @Override
    public String toString() {
        return "Genre : " + name +
                "\n\r Books : " + books;
    }
}
