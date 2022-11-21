package entities.users;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.Account;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

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
        AccountID expectedAccountID = new AccountID(null);
        expectedAccountID.generateID();
        String expectedUsername = "username";
        String expectedPassword = "ABS123!";
        Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());

        // Action
        account = new Account(expectedAccountID, expectedUsername, expectedPassword, expectedTimestamp);

        // Assert messages
        String getConstructorMessage = "The given parameters did not match the expected parameters";

        // Asserts
        Assertions.assertEquals(expectedAccountID, account.getAccountIDObject(), getConstructorMessage);
        Assertions.assertEquals(expectedUsername, account.getUsername(), getConstructorMessage);
        Assertions.assertEquals(expectedPassword, account.getPassword(), getConstructorMessage);
        Assertions.assertEquals(expectedTimestamp, account.getTimestamp(), getConstructorMessage);
        Assertions.assertNull(account.getSessionID(), getConstructorMessage);
    }

    @Test
    public void testSessionIDConstructor() {
        // Expected values
        SessionID expectedSessionID = new SessionID(null);
        expectedSessionID.generateID();
        String expectedUsername = "username";
        String expectedPassword = "ABS123!";

        // Action
        account = new Account(expectedSessionID, expectedUsername, expectedPassword);

        // Assert messages
        String getConstructorMessage = "The given parameters did not match the expected parameters";

        // Asserts
        Assertions.assertEquals(expectedSessionID, account.getSessionIDObject(), getConstructorMessage);
        Assertions.assertEquals(expectedUsername, account.getUsername(), getConstructorMessage);
        Assertions.assertEquals(expectedPassword, account.getPassword(), getConstructorMessage);
        Assertions.assertNull(account.getTimestamp(), getConstructorMessage);
        Assertions.assertNull(account.getAccountID(), getConstructorMessage);
    }

    @Test
    public void testGetAccountID() {
        // Expected values
        AccountID expectedAccountID = new AccountID(null);
        expectedAccountID.generateID();
        String expectedUsername = "username";
        String expectedPassword = "ABS123!";
        Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());

        // Action
        account = new Account(expectedAccountID, expectedUsername, expectedPassword, expectedTimestamp);

        // Assert messages
        String getConstructorMessage = "The given parameters did not match the expected parameters";

        // Asserts
        Assertions.assertEquals(expectedAccountID, account.getAccountIDObject(), getConstructorMessage);
    }

    @Test
    public void testGetSessionID() {
        // Expected values
        SessionID expectedSessionID = new SessionID(null);
        expectedSessionID.generateID();
        String expectedUsername = "username";
        String expectedPassword = "ABS123!";

        // Action
        account = new Account(expectedSessionID, expectedUsername, expectedPassword);

        // Assert messages
        String getConstructorMessage = "The given parameters did not match the expected parameters";

        // Asserts
        Assertions.assertEquals(expectedSessionID, account.getSessionIDObject(), getConstructorMessage);
    }

    @Test
    public void testGetPassword() {
        // Expected values
        SessionID expectedSessionID = new SessionID(null);
        expectedSessionID.generateID();
        String expectedUsername = "username";
        String expectedPassword = "ABS123!";

        // Action
        account = new Account(expectedSessionID, expectedUsername, expectedPassword);

        // Assert messages
        String getConstructorMessage = "The given parameters did not match the expected parameters";

        // Asserts
        Assertions.assertEquals(expectedPassword, account.getPassword(), getConstructorMessage);
    }

    @Test
    public void testSetPassword() {
        // Expected values
        SessionID expectedSessionID = new SessionID(null);
        expectedSessionID.generateID();
        String expectedUsername = "username";
        String expectedPassword = "ApplePie22!";

        // Action
        account = new Account(expectedSessionID, expectedUsername, "ABC123!");
        account.setPassword("ApplePie22!");

        // Assert messages
        String getConstructorMessage = "The given parameters did not match the expected parameters";

        // Asserts
        Assertions.assertEquals(expectedPassword, account.getPassword(), getConstructorMessage);
    }
}
