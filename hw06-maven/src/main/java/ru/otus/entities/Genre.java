package ru.otus.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

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
            generator = "authorsSeq")
    @SequenceGenerator(
            name = "genresSeq",
            allocationSize = 1,
            sequenceName = "genres_seq")
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
}
