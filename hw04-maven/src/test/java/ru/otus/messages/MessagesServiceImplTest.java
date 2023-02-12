package ru.otus.messages;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.MessageSource;
import ru.otus.config.AppPropertiesConfig;
import ru.otus.config.PropertiesConfig;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
class MessagesServiceImplTest {

    private final static String MESSAGE = "What is your name?";
    private final static String MESSAGE_NAME = "ask.name";

    @Autowired
    private MessageSource messageSource;

    @SpyBean
    PropertiesConfig config;

    @Test
    void getMessage() {
        AppPropertiesConfig config = mock(AppPropertiesConfig.class);
        Mockito.when(config.getLocale()).thenReturn(new Locale("en"));
        MessagesService messagesService = new MessagesServiceImpl(messageSource, config);

        assertEquals(MESSAGE, messagesService.getMessage(MESSAGE_NAME));
    }
}