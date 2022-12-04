package entities;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.Invitation;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class InvitationTest {

    Invitation invitation;

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    // Test the Invitations Constructor
    @Test
    public void testInvitationsConstructor() {
        // Expected values
        AccountID expectedSenderIDObject = new AccountID(null);
        expectedSenderIDObject.generateID();

        AccountID expectedReceiverIDObject = new AccountID(null);
        expectedReceiverIDObject.generateID();

        Date expectedTimestamp = new Date(System.currentTimeMillis());

        // Action
        invitation = new Invitation(expectedSenderIDObject, expectedReceiverIDObject, expectedTimestamp);

        // Assert messages
        String getConstructorMessage = "The given parameters did not match the expected parameters";

        Assertions.assertEquals(expectedSenderIDObject, invitation.getSenderIDObject(), getConstructorMessage);
        Assertions.assertEquals(expectedReceiverIDObject, invitation.getReceiverIDObject(), getConstructorMessage);
        Assertions.assertEquals(expectedTimestamp, invitation.getTimestamp(), getConstructorMessage);
        Assertions.assertEquals(expectedSenderIDObject.getID(), invitation.getSenderID(), getConstructorMessage);
        Assertions.assertEquals(expectedReceiverIDObject.getID(), invitation.getReceiverID(), getConstructorMessage);

    }

    // Test getReceiverID
    @Test
    public void testGetReceiverID() {
        // Expected values
        AccountID expectedSenderIDObject = new AccountID(null);
        expectedSenderIDObject.generateID();

        AccountID expectedReceiverIDObject = new AccountID(null);
        expectedReceiverIDObject.generateID();

        Date expectedTimestamp = new Date(System.currentTimeMillis());

        // Action
        invitation = new Invitation(expectedSenderIDObject, expectedReceiverIDObject, expectedTimestamp);

        // Assert messages
        String getConstructorMessage = "The given parameters did not match the expected parameters";

        Assertions.assertEquals(expectedReceiverIDObject.getID(), invitation.getReceiverID(), getConstructorMessage);
        Assertions.assertNotEquals("", expectedReceiverIDObject.getID(), getConstructorMessage);
    }
    // Test getSenderID
    @Test
    public void testGetSenderID() {
        // Expected values
        AccountID expectedSenderIDObject = new AccountID(null);
        expectedSenderIDObject.generateID();

        AccountID expectedReceiverIDObject = new AccountID(null);
        expectedReceiverIDObject.generateID();

        Date expectedTimestamp = new Date(System.currentTimeMillis());

        // Action
        invitation = new Invitation(expectedSenderIDObject, expectedReceiverIDObject, expectedTimestamp);

        // Assert messages
        String getConstructorMessage = "The given parameters did not match the expected parameters";

        Assertions.assertEquals(expectedSenderIDObject.getID(), invitation.getSenderID(), getConstructorMessage);
        Assertions.assertNotEquals("", expectedSenderIDObject.getID(), getConstructorMessage);

    }
    // Test getSenderIDObject
    @Test
    public void testGetSenderIDObject() {
        AccountID expectedSenderIDObject = new AccountID(null);
        expectedSenderIDObject.generateID();

        AccountID expectedReceiverIDObject = new AccountID(null);
        expectedReceiverIDObject.generateID();

        Date expectedTimestamp = new Date(System.currentTimeMillis());

        // Action
        invitation = new Invitation(expectedSenderIDObject, expectedReceiverIDObject, expectedTimestamp);

        // Assert messages
        String getConstructorMessage = "The given parameters did not match the expected parameters";

        Assertions.assertEquals(expectedSenderIDObject, invitation.getSenderIDObject(), getConstructorMessage);
        Assertions.assertNotEquals(new AccountID(null), expectedSenderIDObject, getConstructorMessage);
    }
    // Test getReceiverIDObject
    @Test
    public void testGetReceiverIDObject() {
        AccountID expectedSenderIDObject = new AccountID(null);
        expectedSenderIDObject.generateID();

        AccountID expectedReceiverIDObject = new AccountID(null);
        expectedReceiverIDObject.generateID();

        Date expectedTimestamp = new Date(System.currentTimeMillis());

        // Action
        invitation = new Invitation(expectedSenderIDObject, expectedReceiverIDObject, expectedTimestamp);

        // Assert messages
        String getConstructorMessage = "The given parameters did not match the expected parameters";

        Assertions.assertEquals(expectedReceiverIDObject, invitation.getReceiverIDObject(), getConstructorMessage);
        Assertions.assertNotEquals(new AccountID(null), expectedReceiverIDObject, getConstructorMessage);
    }
    // getTimeStamp
    @Test
    public void testGetTimeStamp() {
        AccountID expectedSenderIDObject = new AccountID(null);
        expectedSenderIDObject.generateID();

        AccountID expectedReceiverIDObject = new AccountID(null);
        expectedReceiverIDObject.generateID();

        Date expectedTimestamp = new Date(System.currentTimeMillis());

        // Action
        invitation = new Invitation(expectedSenderIDObject, expectedReceiverIDObject, expectedTimestamp);

        // Assert messages
        String getConstructorMessage = "The given parameters did not match the expected parameters";

        Assertions.assertEquals(expectedTimestamp, invitation.getTimestamp(), getConstructorMessage);
    }

}
