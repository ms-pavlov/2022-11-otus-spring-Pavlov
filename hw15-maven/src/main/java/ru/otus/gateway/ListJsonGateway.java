package ru.otus.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(errorChannel = "handledErrorChannel")
public interface ListJsonGateway {

    @Gateway(requestChannel = "listJsonChannel", replyChannel = "outputChannel")
    String process(String json);
}
