package ru.otus.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

}
