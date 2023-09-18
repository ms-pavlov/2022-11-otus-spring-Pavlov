package ru.otus.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(errorChannel = "handledErrorChannel")
public interface JsonGateway {

    @Gateway(requestChannel = "jsonChannel", replyChannel = "outputChannel")
    String process(String json);
}
