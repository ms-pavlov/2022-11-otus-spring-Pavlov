package ru.otus.views;

public interface TestView {

    String getUserName();

    int getUserAnswer();

    void showResult(String name, int total, int error);
}
