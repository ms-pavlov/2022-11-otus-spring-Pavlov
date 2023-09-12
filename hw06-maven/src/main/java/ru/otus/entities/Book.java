package ru.otus.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "booksSeq")
    @SequenceGenerator(
            name = "booksSeq",
            allocationSize = 1,
            sequenceName = "books_seq")
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(name = "books", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authors", referencedColumnName = "id"))
    private List<Author> authors;
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "books_genres",
            joinColumns = @JoinColumn(name = "books", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genres", referencedColumnName = "id"))
    private List<Genre> genres;

}

