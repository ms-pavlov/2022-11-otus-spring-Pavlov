package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BooksResponse {
    private final String id;
    private final String name;
    private final List<String> authors;
    private final List<String> genres;

    @Override
    public String toString() {
        return " Title: " + name
                + "\n\r Authors: " + authors
                + "\n\r Genres: " + genres
                + "\n\r Id: " + id + "\n\r";
    }
}
