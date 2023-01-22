package ru.otus.messages;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.config.AppPropertiesConfig;
import ru.otus.config.PropertiesConfig;

import java.util.Locale;

@Service
public class MessagesServiceImpl implements MessagesService {
    private final MessageSource messageSource;
    private final Locale locale;

    public MessagesServiceImpl(MessageSource messageSource, PropertiesConfig config) {
        this.messageSource = messageSource;
        this.locale = config.getLocale();
    }

    @Override
    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }
}
