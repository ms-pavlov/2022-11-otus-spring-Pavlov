package ru.otus.securities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record User(
        String username,
        String password,
        List<String> accesses) {

    public User(String username, String password) {
        this(username, password, new ArrayList<>());
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

    public String getDefaultAccess() {
        return accesses.stream().findAny()
                .orElse("default");
    }
}
