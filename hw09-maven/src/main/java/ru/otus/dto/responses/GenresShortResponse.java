package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenresShortResponse {

    private final Long id;
    private final String name;
}
