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
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "usersSeq")
    @SequenceGenerator(
            name = "usersSeq",
            allocationSize = 1,
            sequenceName = "users_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Authority> authorities;

    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public User(String name, String login, String password, List<Authority> authorities) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.authorities = authorities;
    }
}
