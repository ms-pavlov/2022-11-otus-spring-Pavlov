package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class AuthorsResponse {
    private final Long id;
    private final String name;
    private final List<BooksResponse> books;

    @Override
    public String toString() {
        return "Name : '" + name
                + "\n\r Bibliography:" +
                books.stream()
                        .map(this::getBibliographyBook)
                        .collect(Collectors.joining("\n\r"));
    }

    private String getBibliographyBook(BooksResponse book) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format("\n\r Title : %s ", book.getName()));

        String coAuthors = book.getAuthors().stream()
                .filter(author -> !author.equals(name))
                .collect(Collectors.joining(", "));
        if (!coAuthors.isEmpty()) {
            stringBuilder.append(String.format("\n\r co - authors : %s ", coAuthors));
        }
        return stringBuilder.toString();
    }
}
