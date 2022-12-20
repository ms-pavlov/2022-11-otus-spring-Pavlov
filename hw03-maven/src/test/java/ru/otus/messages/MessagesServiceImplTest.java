package ru.otus.messages;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessagesServiceImplTest {

    private final static String MESSAGE = "What is your name?";
    private final static String MESSAGE_NAME = "ask.name";

    @Autowired
    private MessageSource messageSource;

    private final Locale locale = new Locale("en");


    @Test
    void getMessage() {
        MessagesService messagesService = new MessagesServiceImpl(messageSource, locale);

        assertEquals(MESSAGE, messagesService.getMessage(MESSAGE_NAME));
    }
}