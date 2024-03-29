package ru.otus.order;

@FunctionalInterface
public interface Answer<T> {

    void ans(T answer);
}
