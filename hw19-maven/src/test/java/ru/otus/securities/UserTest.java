package ru.otus.securities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.model.entities.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final static String TEST_SCOPE = "TEST_SCOPE";
    private final static String USER_NAME = "test1";
    private final static String PASSWORD = "test2";

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(USER_NAME, PASSWORD);
    }

    @Test
    void username() {
        assertEquals(USER_NAME, user.getLogin());
    }

    @Test
    void password() {
        assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    void addAndHasAccess() {
        assertFalse(user.hasAccess(TEST_SCOPE));
        assertDoesNotThrow(() -> user.addAccess(TEST_SCOPE));
        assertTrue(user.hasAccess(TEST_SCOPE));
    }

    @Test
    void accesses() {
        assertNotNull(user.getAccesses());
    }

    @Test
    void getGameAccesses() {
        user.addAccess(TEST_SCOPE);

        var result = user.getAccesses();

        assertEquals(List.of(TEST_SCOPE), result);
    }
}