package ru.otus.dto.responses;

import lombok.Data;
import ru.otus.entities.Authors;
import ru.otus.entities.Books;
import ru.otus.entities.Genres;

import java.util.List;

@Data
public class BooksResponse {
    private final Long id;
    private final String name;
    private final List<String> authors;
    private final List<String> genres;

    public BooksResponse(Books book) {
        this.id = book.getId();
        this.name = book.getName();
        this.authors = book.getAuthors()
                .stream()
                .map(Authors::name)
                .toList();
        this.genres = book.getGenres()
                .stream()
                .map(Genres::name)
                .toList();
    }

    @Override
    public String toString() {
        return " Title: " + name
               + "\n\r Authors: " + authors
               + "\n\r Genres: " + genres
               + "\n\r Id: " + id + "\n\r";
    }
}
