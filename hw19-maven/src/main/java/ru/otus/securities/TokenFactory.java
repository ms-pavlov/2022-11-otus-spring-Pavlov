package ru.otus.securities;

public interface TokenFactory {

    String create(String scope, User user);

}
