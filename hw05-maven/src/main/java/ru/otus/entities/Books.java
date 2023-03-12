package ru.otus.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Books {

    private Long id;
    private String name;
    private List<Authors> authors;
    private List<Genres> genres;

}

