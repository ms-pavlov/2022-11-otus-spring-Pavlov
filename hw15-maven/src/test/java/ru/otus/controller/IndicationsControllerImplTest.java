package ru.otus.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.gateway.JsonGateway;

import java.io.PrintStream;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        IndicationsControllerImpl.class
})
class IndicationsControllerImplTest {

    private final static String JSON = "JSON";
    private final static String ANSWER = "ANSWER";

    @MockBean
    private JsonGateway jsonGateway;
    @MockBean
    private PrintStream printStream;


    @Autowired
    private IndicationsController controller;

    @Test
    @DisplayName("метод send отправляет json в JsonGateway, а полученный ответ печатает с помощью PrintStream")
    void send() {
        when(jsonGateway.process(JSON)).thenReturn(ANSWER);

        controller.send(JSON);

        verify(jsonGateway, times(1)).process(JSON);
        verify(printStream, times(1)).println(ANSWER);
    }
}