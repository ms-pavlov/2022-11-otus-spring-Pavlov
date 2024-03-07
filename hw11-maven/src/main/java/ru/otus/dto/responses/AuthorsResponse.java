package ru.otus.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorsResponse {
    private final String id;
    private final String name;

    @Override
    public String toString() {
        return "\n\rName : " + name
                + "\n\r Id: " + id + "\n\r";
    }
}
