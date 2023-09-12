package ru.otus.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Genre implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "genresSeq")
    @SequenceGenerator(
            name = "genresSeq",
            allocationSize = 1,
            sequenceName = "genres_seq")
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "books_genres",
            joinColumns = @JoinColumn(name = "books", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genres", referencedColumnName = "id"))
    private List<Book> books;

    public Genre(Long id, String name) {
        this(id, name, new ArrayList<>());
    }
}
