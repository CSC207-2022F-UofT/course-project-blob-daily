package entities.account;

import com.backend.entities.users.Account;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {
    Account account;

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    @Test
    public void testAccountIDConstructor() {
        // Expected values


        // Action

        // Assert messages

        // Asserts
    }

    @Test
    public void testSessionIDConstructor() {
        // Expected values

        // Action

        // Assert messages

        // Asserts
    }

    @Test
    public void testGetAccountID() {
        // Expected values

        // Action

        // Assert messages

        // Asserts
    }

    @Test
    public void testGetSessionID() {
        // Expected values

        // Action

        // Assert messages

        // Asserts
    }

    @Test
    public void testGetPassword() {
        // Expected values

        // Action

        // Assert messages

        // Asserts
    }

    @Test
    public void testSetPassword() {
        // Expected values

        // Action

        // Assert messages

        // Asserts
    }
}
