package ru.otus.services.exceptions;

public class UnknownListJsonFormatException extends RuntimeException{
    public UnknownListJsonFormatException(Throwable cause) {
        super(cause);
    }
}
