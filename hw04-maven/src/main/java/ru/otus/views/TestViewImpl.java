package ru.otus.views;

import org.springframework.stereotype.Component;
import ru.otus.messages.MessagesService;

import java.io.PrintStream;
import java.util.Scanner;

@Component
public class TestViewImpl implements TestView {
    private final PrintStream printStream;
    private final Scanner scanner;

    private final MessagesService messagesService;

    public TestViewImpl(PrintStream printStream, Scanner scanner, MessagesService messagesService) {
        this.printStream = printStream;
        this.scanner = scanner;
        this.messagesService = messagesService;
    }

    @Override
    public String getUserName() {
        printStream.print(messagesService.getMessage("ask.name"));
        return scanner.next();
    }

    @Override
    public int getUserAnswer() {
        printStream.print(messagesService.getMessage("ask.answer"));
        return scanner.nextInt();
    }

    @Override
    public void showResult(String name, int total, int error) {
        printStream.println(messagesService.getMessage("result", name, total, total - error, error));
    }
}
