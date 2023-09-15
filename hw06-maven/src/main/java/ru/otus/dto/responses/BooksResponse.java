package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;

import java.util.List;

@Data
@AllArgsConstructor
public class BooksResponse {
    private final Long id;
    private final String name;
    private final List<String> authors;
    private final List<String> genres;

    public BooksResponse(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.authors = book.getAuthors()
                .stream()
                .map(Author::getName)
                .toList();
        this.genres = book.getGenres()
                .stream()
                .map(Genre::getName)
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
