package ru.otus.views;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

import static org.mockito.Mockito.*;

class TestViewImplTest {

    private final static Map<String, String> PROPERTIES = Map.of("result", "total : %d, right : %d, error : %d%n",
            "ask", "input a number: ");
    private final static String USER_NAME = "user";

    private PrintStream printStream;
    private Scanner scanner;

    @BeforeEach
    void setUp() {
        printStream = mock(PrintStream.class);
        scanner = mock(Scanner.class);
    }


    @Test
    void getUserAnswer() {
        var view = new TestViewImpl(printStream, scanner, PROPERTIES);

        view.getUserAnswer();

        verify(printStream, times(1)).print(PROPERTIES.get("ask"));
        verify(scanner, times(1)).nextInt();
    }

    @Test
    void showResult() {
        var view = new TestViewImpl(printStream, scanner, PROPERTIES);

        view.showResult(USER_NAME,1, 1);

        verify(printStream, times(1)).printf(PROPERTIES.get("result"), USER_NAME, 1, 0, 1);
    }
}