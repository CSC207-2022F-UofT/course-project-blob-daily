package entities.account;

import backend.entities.IDs.AccountID;
import backend.entities.criteria.Criteria;
import backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountIDTest {
    AccountID accountID;

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
        String expectedDefaultID = "p0LkdlajijdILD897&*8";

        // Action
        accountID = new AccountID(expectedDefaultID);
        String actualAccountID = accountID.getID();

        // Assert messages
        String getConstructorMessage = String.format("The given ID %s was returned instead of the expect %s",
                actualAccountID, expectedDefaultID);

        // Asserts
        Assertions.assertEquals(actualAccountID, expectedDefaultID, getConstructorMessage);
    }

    @Test
    public void testGetAccountID() {
        // Expected values
        String expectedDefaultID = "p0LkdlajijdILD897&*8";

        // Action
        accountID = new AccountID(expectedDefaultID);
        String actualAccountID = accountID.getID();

        // Assert messages
        String getAccountIDMessage = String.format("The given ID %s was returned instead of the expect %s",
                actualAccountID, expectedDefaultID);

        // Asserts
        Assertions.assertEquals(actualAccountID, expectedDefaultID, getAccountIDMessage);
    }

    @Test
    public void testIsValid() {
        // Expected values
        String expectedDefaultID = "p0LkdlajijdILD897&*8";

        // Action
        accountID = new AccountID(expectedDefaultID);

        // Assert messages
        String getValidMessage = String.format("The given ID %s was unexpectedly found invalid",
                expectedDefaultID);

        // Asserts
        Assertions.assertTrue(accountID.isValid(), getValidMessage);
    }

    @Test
    public void testInvalidAccountIDSizeTooSmall() {
        // Expected values
        String invalidDefaultID = "&*8adD";

        // Action
        accountID = new AccountID(invalidDefaultID);

        // Assert messages
        String invalidAccountIDMessage = String.format("An expected invalid ID %s was found to be valid",
                invalidDefaultID);

        // Asserts
        Assertions.assertFalse(accountID.isValid(), invalidAccountIDMessage);
    }

    @Test
    public void testInvalidAccountIDSizeTooLarge() {
        // Expected values
        String invalidDefaultID = "&*8adDdlkamdklamkdmKDMALKDMdad22ed";

        // Action
        accountID = new AccountID(invalidDefaultID);

        // Assert messages
        String invalidAccountIDMessage = String.format("An expected invalid ID %s was found to be valid",
                invalidDefaultID);

        // Asserts
        Assertions.assertFalse(accountID.isValid(), invalidAccountIDMessage);
    }

    @Test
    public void testInvalidAccountIDContainsIllegalValue() {
        // Expected values
        String invalidDefaultID = "IDliadjl88*🫡kkkkkkk";

        // Action
        accountID = new AccountID(invalidDefaultID);

        // Assert messages
        String invalidAccountIDMessage = String.format("An expected invalid ID %s was found to be valid",
                invalidDefaultID);

        // Asserts
        Assertions.assertFalse(accountID.isValid(), invalidAccountIDMessage);
    }

    @Test
    public void testInvalidAccountIDMissingValue() {
        // Expected values
        String invalidDefaultID = "IDliadjl88kkkkwkkkkk";

        // Action
        accountID = new AccountID(invalidDefaultID);

        // Assert messages
        String invalidAccountIDMessage = String.format("An expected invalid ID %s was found to be valid",
                invalidDefaultID);

        // Asserts
        Assertions.assertFalse(accountID.isValid(), invalidAccountIDMessage);
    }

    @Test
    public void testGetCriteria() {
        // Expected values
        String expectedDefaultID = "p0LkdlajijdILD897&*8";
        Criteria expectedCriteria;

        // Action
        accountID = new AccountID(expectedDefaultID);
        expectedCriteria = accountID.getCriteria();
        Criteria actualCriteria = AccountID.criteria;

        // Assert messages
        String getCriteriaMessage = String.format("The return criteria %s doesn't match the expected criteria %s",
                actualCriteria, expectedCriteria);

        // Asserts
        Assertions.assertEquals(actualCriteria, expectedCriteria, getCriteriaMessage);
    }

    @Test
    public void testGenerateAccountID() {
        // Expected values
        String expectedDefaultID = null;

        // Action
        accountID = new AccountID(expectedDefaultID);
        accountID.generateID();

        // Assert messages
        String generateAccountIDMessage = String.format("The generated accountID %s is not valid",
                accountID.getID());

        // Asserts
        Assertions.assertTrue(accountID.isValid(), generateAccountIDMessage);
    }
}
