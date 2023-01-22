package ru.otus.views;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.messages.MessagesService;

import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

import static org.mockito.Mockito.*;

@SpringBootTest
class TestViewImplTest {

    private final static Map<String, String> PROPERTIES = Map.of("result", "total : %d, right : %d, error : %d%n",
            "ask", "input a number: ");
    private final static String USER_NAME = "user";

    @MockBean
    private PrintStream printStream;
    @MockBean
    private Scanner scanner;
    @MockBean
    MessagesService messagesService;


    @Test
    void getUserName() {
        var view = new TestViewImpl(printStream, scanner, messagesService);
        when(messagesService.getMessage("ask.name")).thenReturn(PROPERTIES.get("ask"));

        view.getUserName();

        verify(printStream, times(1)).print(PROPERTIES.get("ask"));
        verify(scanner, times(1)).next();
    }


    @Test
    void getUserAnswer() {
        var view = new TestViewImpl(printStream, scanner, messagesService);
        when(messagesService.getMessage("ask.answer")).thenReturn(PROPERTIES.get("ask"));

        view.getUserAnswer();

        verify(printStream, times(1)).print(PROPERTIES.get("ask"));
        verify(scanner, times(1)).nextInt();
    }

    @Test
    void showResult() {
        var view = new TestViewImpl(printStream, scanner, messagesService);
        when(messagesService.getMessage("result", USER_NAME,1, 0, 1)).thenReturn(PROPERTIES.get("result"));

        view.showResult(USER_NAME,1, 1);

        verify(printStream, times(1)).println(PROPERTIES.get("result"));
    }
}