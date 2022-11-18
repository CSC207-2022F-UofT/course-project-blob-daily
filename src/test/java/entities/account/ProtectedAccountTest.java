package entities.account;


import com.backend.error.handlers.LogHandler;
import com.backend.entities.users.ProtectedAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

public class ProtectedAccountTest {
    ProtectedAccount protectedAccount;

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    @Test
    public void testSingleVarConstructor() {
        // Expected values
        String expectedUsername = "ShaanP22";

        // Action
        protectedAccount = new ProtectedAccount(expectedUsername);

        // Assert messages
        String timestampMessage = String.format("The timestamp was not initialized correctly, given %s and wanted %s",
                protectedAccount.getTimestamp(), null);
        String usernameMessage = String.format("The username was not initialized correctly, given %s and wanted %s",
                protectedAccount.getUsername(), expectedUsername);

        // Asserts
        Assertions.assertNull(protectedAccount.getTimestamp(), timestampMessage);
        Assertions.assertEquals(expectedUsername, protectedAccount.getUsername().toString(), usernameMessage);
    }

    @Test
    public void testDoubleVarConstructor() {
        // Expected values
        String expectedUsername = "ShaanP22";
        Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());

        // Action
        protectedAccount = new ProtectedAccount(expectedUsername, expectedTimestamp);

        // Assert messages
        String timestampMessage = String.format("The timestamp was not initialized correctly, given %s and wanted %s",
                protectedAccount.getTimestamp(), expectedTimestamp);
        String usernameMessage = String.format("The username was not initialized correctly, given %s and wanted %s",
                protectedAccount.getUsername(), expectedUsername);

        // Asserts
        Assertions.assertEquals(expectedTimestamp, protectedAccount.getTimestamp(), timestampMessage);
        Assertions.assertEquals(expectedUsername, protectedAccount.getUsername().toString(), usernameMessage);
    }

    @Test
    public void testInvalidUsernameSizeTooSmall() {
        // Expected values
        String invalidUsername = "Sh3";
        Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());

        // Action
        protectedAccount = new ProtectedAccount(invalidUsername, expectedTimestamp);

        // Assert messages
        String invalidUsernameMessage = String.format("An expected invalid username %s was found to be valid",
                invalidUsername);

        // Asserts
        Assertions.assertFalse(protectedAccount.getUsername().isValid(), invalidUsernameMessage);
    }

    @Test
    public void testInvalidUsernameSizeToobig() {
        // Expected values
        String invalidUsername = "d0oaod0adoakdkLIJD223";
        Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());

        // Action
        protectedAccount = new ProtectedAccount(invalidUsername, expectedTimestamp);

        // Assert messages
        String invalidUsernameMessage = String.format("An expected invalid username %s was found to be valid",
                invalidUsername);

        // Asserts
        Assertions.assertFalse(protectedAccount.getUsername().isValid(), invalidUsernameMessage);
    }

    @Test
    public void testInvalidUsernameContainsIllegalValue() {
        // Expected values
        String invalidUsername = "IDliadjl88*";
        Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());

        // Action
        protectedAccount = new ProtectedAccount(invalidUsername, expectedTimestamp);

        // Assert messages
        String invalidUsernameMessage = String.format("An expected invalid username %s was found to be valid",
                invalidUsername);

        // Asserts
        Assertions.assertFalse(protectedAccount.getUsername().isValid(), invalidUsernameMessage);
    }

    @Test
    public void testInvalidUsernameMissingValue() {
        // Expected values
        String invalidUsername = "jdaidj82";
        Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());

        // Action
        protectedAccount = new ProtectedAccount(invalidUsername, expectedTimestamp);

        // Assert messages
        String invalidUsernameMessage = String.format("An expected invalid username %s was found to be valid",
                invalidUsername);

        // Asserts
        Assertions.assertFalse(protectedAccount.getUsername().isValid(), invalidUsernameMessage);
    }

    @Test
    public void testInvalidTimestamp() {
        // Expected values
        String expectedUsername = "ShaanP22*";
        String expectedTimestamp = "jdnajdna,:djnad:d ad /da/";

        // Action (included in assert statement)

        // Assert messages
        String invalidTimestampMessage = String.format("An expected exception was never thrown given the invalid timestamp %s",
                expectedTimestamp);

        // Asserts
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Timestamp.valueOf(expectedTimestamp);
        }, invalidTimestampMessage);
    }
}
