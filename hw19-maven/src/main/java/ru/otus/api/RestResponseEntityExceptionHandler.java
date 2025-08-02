package ru.otus.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.openapi.model.Errors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Errors> handle(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(
                new Errors().putErrorsItem("message", ex.getMessage()));
    }
}
