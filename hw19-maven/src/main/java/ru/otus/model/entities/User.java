package ru.otus.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Document(collection = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String name;
    private String login;
    private String password;
    private String currentScope;
    private List<String> accesses;
    private List<String> scopes;

    public User(String username, String password) {
        this(null, username, username, password, "default", new ArrayList<>(), new ArrayList<>());
    }

    public void addAccess(String scope) {
        if (!accesses.contains(scope)) {
            accesses.add(scope);
        }
    }

    public boolean hasAccess(String scope) {
        return Optional.ofNullable(scope)
                .map(accesses::contains)
                .orElse(false);
    }

    public List<String> getAccesses() {
        return new ArrayList<>(accesses);
    }

}
