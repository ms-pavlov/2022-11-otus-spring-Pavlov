package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BooksResponse {
    private String id;
    private String name;
    private List<String> authors;
    private List<String> genres;

    @Override
    public String toString() {
        return " Title: " + name
                + "\n\r Authors: " + authors
                + "\n\r Genres: " + genres
                + "\n\r Id: " + id + "\n\r";
    }
}
