package entities.users.info;

import com.backend.entities.criteria.Criteria;
import com.backend.entities.users.info.Username;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UsernameTest {
    Username username;

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
        String expectedUsername = "p0LkdlajijdILD9";

        // Action
        username = new Username(expectedUsername);
        String actualUsername = username.toString();

        // Assert messages
        String getConstructorMessage = String.format("The given Username %s was returned instead of the expect %s",
                actualUsername, expectedUsername);

        // Asserts
        Assertions.assertEquals(actualUsername, expectedUsername, getConstructorMessage);
    }

    @Test
    public void testGetUsername() {
        // Expected values
        String expectedUsername = "p0LkdlajijdILD9";

        // Action
        username = new Username(expectedUsername);
        String actualUsername = username.toString();

        // Assert messages
        String getConstructorMessage = String.format("The given Username %s was returned instead of the expect %s",
                actualUsername, expectedUsername);

        // Asserts
        Assertions.assertEquals(actualUsername, expectedUsername, getConstructorMessage);
    }

    @Test
    public void testIsValid() {
        // Expected values
        String expectedUsername = "p0LkdlajijdILD9";

        // Action
        username = new Username(expectedUsername);

        // Assert messages
        String getValidMessage = String.format("The given Username %s was unexpectedly found invalid",
                expectedUsername);

        // Asserts
        Assertions.assertTrue(username.isValid(), getValidMessage);
    }

    @Test
    public void testInvalidUsernameSizeTooSmall() {
        // Expected values
        String invalidUsername = "ddD";

        // Action
        username = new Username(invalidUsername);

        // Assert messages
        String invalidUsernameMessage = String.format("An expected invalid Username %s was found to be valid",
                invalidUsername);

        // Asserts
        Assertions.assertFalse(username.isValid(), invalidUsernameMessage);
    }

    @Test
    public void testInvalidUsernameSizeTooLarge() {
        // Expected values
        String invalidUsername = "8adDdlkamdklamkdmKDMALKDMdad22ed";

        // Action
        username = new Username(invalidUsername);

        // Assert messages
        String invalidUsernameMessage = String.format("An expected invalid Username %s was found to be valid",
                invalidUsername);

        // Asserts
        Assertions.assertFalse(username.isValid(), invalidUsernameMessage);
    }

    @Test
    public void testInvalidUsernameContainsIllegalValue() {
        // Expected values
        String invalidUsername = "IDliadjl88*🫡kkk";

        // Action
        username = new Username(invalidUsername);

        // Assert messages
        String invalidUsernameMessage = String.format("An expected invalid Username %s was found to be valid",
                invalidUsername);

        // Asserts
        Assertions.assertFalse(username.isValid(), invalidUsernameMessage);
    }

    @Test
    public void testInvalidUsernameMissingValue() {
        // Expected values
        String invalidUsername = "25425253535";

        // Action
        username = new Username(invalidUsername);

        // Assert messages
        String invalidUsernameMessage = String.format("An expected invalid Username %s was found to be valid",
                invalidUsername);

        // Asserts
        Assertions.assertFalse(username.isValid(), invalidUsernameMessage);
    }

    @Test
    public void testGetCriteria() {
        // Expected values
        String expectedUsername = "p0LkdlajijdILD89";
        Criteria expectedCriteria;

        // Action
        username = new Username(expectedUsername);
        expectedCriteria = Username.criteria;
        Criteria actualCriteria = Username.criteria;

        // Assert messages
        String getCriteriaMessage = String.format("The return criteria %s doesn't match the expected criteria %s",
                actualCriteria, expectedCriteria);

        // Asserts
        Assertions.assertEquals(actualCriteria, expectedCriteria, getCriteriaMessage);
    }

    @Test
    public void testGenerateUsername() {
        // Expected values (Not required)

        // Action
        username = new Username(null);
        username.generateUsername();

        // Assert messages
        String generateUsernameMessage = String.format("The generated Username %s is not valid",
                username.toString());

        // Asserts
        Assertions.assertTrue(username.isValid(), generateUsernameMessage);
    }
}
