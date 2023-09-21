package ru.otus.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.MessageHandlingException;

@MessagingGateway(errorChannel = "handledErrorChannel")
public interface ExceptionGateway {

    @Gateway(requestChannel = "handledErrorChannel")
    void process(MessageHandlingException handlingException);
}
