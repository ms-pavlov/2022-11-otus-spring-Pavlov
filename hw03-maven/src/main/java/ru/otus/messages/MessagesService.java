package ru.otus.messages;

public interface MessagesService {

    String getMessage(String code, Object... args);
}
