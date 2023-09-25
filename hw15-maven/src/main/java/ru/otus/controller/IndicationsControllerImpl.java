package ru.otus.controller;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.gateway.JsonGateway;

import java.io.PrintStream;

@ShellComponent
public class IndicationsControllerImpl implements IndicationsController {

    private final JsonGateway jsonGateway;
    private final PrintStream printStream;

    public IndicationsControllerImpl(JsonGateway jsonGateway, PrintStream printStream) {
        this.jsonGateway = jsonGateway;
        this.printStream = printStream;
    }

    @Override
    @ShellMethod(value = "Send new indication", key = {"send"})
    public void send(String json) {
        printStream.println(jsonGateway.process(json));
    }
}
