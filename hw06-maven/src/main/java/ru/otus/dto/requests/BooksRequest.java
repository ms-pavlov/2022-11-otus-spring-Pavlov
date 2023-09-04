package ru.otus.dto.requests;

import java.util.List;

public record BooksRequest(String name, List<String> authors, List<String> genres) {
}
