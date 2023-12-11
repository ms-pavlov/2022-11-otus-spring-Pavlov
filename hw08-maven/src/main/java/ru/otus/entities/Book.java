package ru.otus.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "books")
@Getter
@Setter
@AllArgsConstructor
public class Book {

    @Id
    private String id;

    private String name;
    @DocumentReference(lazy = true)
    private List<Author> authors;
    @DocumentReference(lazy = true)
    private List<Genre> genres;

    public Book() {
        this.authors = new ArrayList<>();
        this.genres = new ArrayList<>();
    }
}

