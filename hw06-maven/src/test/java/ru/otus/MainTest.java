package ru.otus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.Main;

@SpringBootTest
class MainTest {

    @Test
    void contextLoads() {
        Main.main();
    }
}