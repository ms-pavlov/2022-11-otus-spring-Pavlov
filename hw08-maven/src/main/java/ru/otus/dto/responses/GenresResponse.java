package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenresResponse {
    private final String id;
    private final String name;

    @Override
    public String toString() {
        return "Genre : " + name +
                "\n\r Id: " + id + "\n\r";
    }
}
