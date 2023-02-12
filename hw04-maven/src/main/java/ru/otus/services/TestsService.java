package ru.otus.services;

public interface TestsService {
    void setName(String name);

    void dropName();

    boolean isNameSet();

    void makeTests();
}
