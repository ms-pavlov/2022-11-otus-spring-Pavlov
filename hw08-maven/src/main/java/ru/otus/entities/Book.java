package ru.otus.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "books")
@Getter
@Setter
@AllArgsConstructor
public class Book implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    private String id;

    private String name;
    @DocumentReference(lazy = true)
    private List<Author> authors;
    @DocumentReference(lazy = true)
    private List<Genre> genres;
    @DocumentReference(lazy = true)
    private List<Comment> comments;

    public Book() {
        this.authors = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.comments = new ArrayList<>();
    }
}

