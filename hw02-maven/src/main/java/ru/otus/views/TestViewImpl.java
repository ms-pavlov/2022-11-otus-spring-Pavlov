package ru.otus.views;

import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

@Component
public class TestViewImpl implements TestView {
    private final PrintStream printStream;
    private final Scanner scanner;

    private final Map<String,String> properties;

    public TestViewImpl(PrintStream printStream, Scanner scanner, Map<String,String> properties) {
        this.printStream = printStream;
        this.scanner = scanner;
        this.properties = properties;
    }

    @Override
    public String getUserName() {
        printStream.print(properties.get("ask.name"));
        return scanner.next();
    }

    @Override
    public int getUserAnswer() {
        printStream.print(properties.get("ask.answer"));
        return scanner.nextInt();
    }

    @Override
    public void showResult(String name, int total, int error) {
        printStream.printf((properties.get("result")), name, total, total - error, error);
    }
}
