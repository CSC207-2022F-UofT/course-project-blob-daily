package entities.account;

import backend.entities.IDs.SessionID;
import backend.entities.criteria.Criteria;
import backend.entities.users.info.Password;
import backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PasswordTest {
    Password password;

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    @Test
    public void testConstructor() {
        // Expected values
        String expectedDefaultID = "p0LkdlajijdILD*9";

        // Action
        password = new Password(expectedDefaultID);
        String actualPassword = password.toString();

        // Assert messages
        String getConstructorMessage = String.format("The given Password %s was returned instead of the expect %s",
                actualPassword, expectedDefaultID);

        // Asserts
        Assertions.assertEquals(actualPassword, expectedDefaultID, getConstructorMessage);
    }

    @Test
    public void testGetPassword() {
        // Expected values
        String expectedPassword = "p0LkdlajijdILD*9";

        // Action
        password = new Password(expectedPassword);
        String actualPassword = password.toString();

        // Assert messages
        String getPasswordMessage = String.format("The given Password %s was returned instead of the expect %s",
                actualPassword, expectedPassword);

        // Asserts
        Assertions.assertEquals(actualPassword, expectedPassword, getPasswordMessage);
    }

    @Test
    public void testIsValid() {
        // Expected values
        String expectedPassword = "p0LkdlajijdILD*9";

        // Action
        password = new Password(expectedPassword);

        // Assert messages
        String getValidMessage = String.format("The given Password %s was unexpectedly found invalid",
                expectedPassword);

        // Asserts
        Assertions.assertTrue(password.isValid(), getValidMessage);
    }

    @Test
    public void testInvalidPasswordSizeTooSmall() {
        // Expected values
        String invalidPassword = "&dD";

        // Action
        password = new Password(invalidPassword);

        // Assert messages
        String invalidPasswordMessage = String.format("An expected invalid Password %s was found to be valid",
                invalidPassword);

        // Asserts
        Assertions.assertFalse(password.isValid(), invalidPasswordMessage);
    }

    @Test
    public void testInvalidPasswordSizeTooLarge() {
        // Expected values
        String invalidPassword = "&*8adDdlkamdklamkdmKDMALKDMdad22ed";

        // Action
        password = new Password(invalidPassword);

        // Assert messages
        String invalidPasswordMessage = String.format("An expected invalid Password %s was found to be valid",
                invalidPassword);

        // Asserts
        Assertions.assertFalse(password.isValid(), invalidPasswordMessage);
    }

    @Test
    public void testInvalidPasswordContainsIllegalValue() {
        // Expected values
        String invalidPassword = "IDliadjl88*🫡kkk";

        // Action
        password = new Password(invalidPassword);

        // Assert messages
        String invalidPasswordMessage = String.format("An expected invalid Password %s was found to be valid",
                invalidPassword);

        // Asserts
        Assertions.assertFalse(password.isValid(), invalidPasswordMessage);
    }

    @Test
    public void testInvalidPasswordMissingValue() {
        // Expected values
        String invalidPassword = "aaliadjl88kkkkwk";

        // Action
        password = new Password(invalidPassword);

        // Assert messages
        String invalidPasswordMessage = String.format("An expected invalid username %s was found to be valid",
                invalidPassword);

        // Asserts
        Assertions.assertFalse(password.isValid(), invalidPasswordMessage);
    }

    @Test
    public void testGetCriteria() {
        // Expected values
        String expectedPassword = "p0LkdlajijdI*LD89";
        Criteria expectedCriteria;

        // Action
        password = new Password(expectedPassword);
        expectedCriteria = Password.getCriteria();
        Criteria actualCriteria = Password.criteria;

        // Assert messages
        String getCriteriaMessage = String.format("The return criteria %s doesn't match the expected criteria %s",
                actualCriteria, expectedCriteria);

        // Asserts
        Assertions.assertEquals(actualCriteria, expectedCriteria, getCriteriaMessage);
    }

    @Test
    public void testGeneratePassword() {
        // Expected values
        String expectedPassword = null;

        // Action
        password = new Password(expectedPassword);
        password.generatePassword();

        // Assert messages
        String generatePasswordMessage = String.format("The generated Password %s is not valid",
                password.toString());

        // Asserts
        Assertions.assertTrue(password.isValid(), generatePasswordMessage);
    }
}
