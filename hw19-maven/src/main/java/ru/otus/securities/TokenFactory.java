package ru.otus.securities;

import ru.otus.model.entities.User;

public interface TokenFactory {

    String create(String scope, User user);

}
