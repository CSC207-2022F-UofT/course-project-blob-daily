package entities.account;

import com.backend.entities.IDs.SessionID;
import com.backend.entities.criteria.Criteria;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SessionIDTest {
    SessionID sessionID;

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
        String expectedDefaultID = "p0LkdlajijdILD89";

        // Action
        sessionID = new SessionID(expectedDefaultID);
        String actualSessionID = sessionID.getID();

        // Assert messages
        String getConstructorMessage = String.format("The given ID %s was returned instead of the expect %s",
                actualSessionID, expectedDefaultID);

        // Asserts
        Assertions.assertEquals(actualSessionID, expectedDefaultID, getConstructorMessage);
    }

    @Test
    public void testGetSessionID() {
        // Expected values
        String expectedDefaultID = "p0LkdlajijdILD89";

        // Action
        sessionID = new SessionID(expectedDefaultID);
        String actualSessionID = sessionID.getID();

        // Assert messages
        String getAccountIDMessage = String.format("The given ID %s was returned instead of the expect %s",
                actualSessionID, expectedDefaultID);

        // Asserts
        Assertions.assertEquals(actualSessionID, expectedDefaultID, getAccountIDMessage);
    }

    @Test
    public void testIsValid() {
        // Expected values
        String expectedDefaultID = "p0LkdlajijdILD89";

        // Action
        sessionID = new SessionID(expectedDefaultID);

        // Assert messages
        String getValidMessage = String.format("The given ID %s was unexpectedly found invalid",
                expectedDefaultID);

        // Asserts
        Assertions.assertTrue(sessionID.isValid(), getValidMessage);
    }

    @Test
    public void testInvalidSessionIDSizeTooSmall() {
        // Expected values
        String invalidDefaultID = "dD";

        // Action
        sessionID = new SessionID(invalidDefaultID);

        // Assert messages
        String invalidSessionIDMessage = String.format("An expected invalid ID %s was found to be valid",
                invalidDefaultID);

        // Asserts
        Assertions.assertFalse(sessionID.isValid(), invalidSessionIDMessage);
    }

    @Test
    public void testInvalidSessionIDSizeTooLarge() {
        // Expected values
        String invalidDefaultID = "am8adDdlkamdklamkdmKDMALKDMdad22ed";

        // Action
        sessionID = new SessionID(invalidDefaultID);

        // Assert messages
        String invalidSessionIDMessage = String.format("An expected invalid username %s was found to be valid",
                invalidDefaultID);

        // Asserts
        Assertions.assertFalse(sessionID.isValid(), invalidSessionIDMessage);
    }

    @Test
    public void testInvalidSessionIDContainsIllegalValue() {
        // Expected values
        String invalidDefaultID = "IDliadjl88*🫡kkk";

        // Action
        sessionID = new SessionID(invalidDefaultID);

        // Assert messages
        String invalidSessionIDMessage = String.format("An expected invalid username %s was found to be valid",
                invalidDefaultID);

        // Asserts
        Assertions.assertFalse(sessionID.isValid(), invalidSessionIDMessage);
    }

    @Test
    public void testInvalidSessionIDMissingValue() {
        // Expected values
        String invalidDefaultID = "aaliadjl88kkkkwk";

        // Action
        sessionID = new SessionID(invalidDefaultID);

        // Assert messages
        String invalidSessionIDMessage = String.format("An expected invalid username %s was found to be valid",
                invalidDefaultID);

        // Asserts
        Assertions.assertFalse(sessionID.isValid(), invalidSessionIDMessage);
    }

    @Test
    public void testGetCriteria() {
        // Expected values
        String expectedDefaultID = "p0LkdlajijdILD89";
        Criteria expectedCriteria;

        // Action
        sessionID = new SessionID(expectedDefaultID);
        expectedCriteria = sessionID.getCriteria();
        Criteria actualCriteria = SessionID.criteria;

        // Assert messages
        String getCriteriaMessage = String.format("The return criteria %s doesn't match the expected criteria %s",
                actualCriteria, expectedCriteria);

        // Asserts
        Assertions.assertEquals(actualCriteria, expectedCriteria, getCriteriaMessage);
    }

    @Test
    public void testGenerateSessionID() {
        // Expected values
        String expectedDefaultID = null;

        // Action
        sessionID = new SessionID(expectedDefaultID);
        sessionID.generateID();

        // Assert messages
        String generateSessionIDMessage = String.format("The generated sessionID %s is not valid",
                sessionID.getID());

        // Asserts
        Assertions.assertTrue(sessionID.isValid(), generateSessionIDMessage);
    }
}
