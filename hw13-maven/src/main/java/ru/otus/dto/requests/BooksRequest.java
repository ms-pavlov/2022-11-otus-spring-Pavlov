package ru.otus.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public final class BooksRequest {

    private final String name;
    private final List<String> authors;
    private final List<String> genres;

    @Override
    public String toString() {
        return "BooksRequest[" +
                "name=" + name + ", " +
                "authors=" + authors + ", " +
                "genres=" + genres + ']';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        BooksRequest request = (BooksRequest) object;

        if (!Objects.equals(name, request.name)) return false;
        if (!Objects.equals(authors, request.authors)) return false;
        return Objects.equals(genres, request.genres);
    }
}
