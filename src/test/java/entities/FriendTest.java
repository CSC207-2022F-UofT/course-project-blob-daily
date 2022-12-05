package entities;

import com.backend.entities.Friend;
import com.backend.entities.IDs.AccountID;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class FriendTest {
    Friend friend;
    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    // test the constructor of friend entity
    @Test
    public void testFriendConstructor() {
        // Expected Values
        AccountID expectedAccountID = new AccountID(null);
        expectedAccountID.generateID();

        AccountID expectedFriendAccountID1 = new AccountID(null);
        expectedFriendAccountID1.generateID();

        AccountID expectedFriendAccountID2 = new AccountID(null);
        expectedFriendAccountID2.generateID();

        AccountID expectedFriendAccountID3 = new AccountID(null);
        expectedFriendAccountID3.generateID();

        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add(expectedFriendAccountID1.getID());
        expectedList.add(expectedFriendAccountID2.getID());
        expectedList.add(expectedFriendAccountID3.getID());

        friend = new Friend(expectedAccountID, expectedList);

        // Assert messages
        String getConstructorMessage = "The given parameters did not match the expected parameters";

        // Assertion Statements
        Assertions.assertEquals(expectedAccountID, friend.getAccountIDObject(), getConstructorMessage);
        Assertions.assertEquals(expectedAccountID.getID(), friend.getAccountID(), getConstructorMessage);
        Assertions.assertEquals(expectedList, friend.getFriends(), getConstructorMessage);
    }


    // test getAccountIDObject
    @Test
    public void testGetAccountIDObject() {
        // Expected Values
        AccountID expectedAccountID = new AccountID(null);
        expectedAccountID.generateID();

        AccountID expectedFriendAccountID1 = new AccountID(null);
        expectedFriendAccountID1.generateID();

        AccountID expectedFriendAccountID2 = new AccountID(null);
        expectedFriendAccountID2.generateID();

        AccountID expectedFriendAccountID3 = new AccountID(null);
        expectedFriendAccountID3.generateID();

        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add(expectedFriendAccountID1.getID());
        expectedList.add(expectedFriendAccountID2.getID());
        expectedList.add(expectedFriendAccountID3.getID());

        // Action
        friend = new Friend(expectedAccountID, expectedList);

        // Assert Message
        String getConstructorMessage = "The actual AccountIDObject does not match with expected AccountIDObject";
        Assertions.assertEquals(expectedAccountID, friend.getAccountIDObject(), getConstructorMessage);

    }
    // test getAccountID
    @Test
    public void testGetAccountID() {
        // Expected Values
        AccountID expectedAccountID = new AccountID(null);
        expectedAccountID.generateID();

        AccountID expectedFriendAccountID1 = new AccountID(null);
        expectedFriendAccountID1.generateID();

        AccountID expectedFriendAccountID2 = new AccountID(null);
        expectedFriendAccountID2.generateID();

        AccountID expectedFriendAccountID3 = new AccountID(null);
        expectedFriendAccountID3.generateID();

        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add(expectedFriendAccountID1.getID());
        expectedList.add(expectedFriendAccountID2.getID());
        expectedList.add(expectedFriendAccountID3.getID());

        // Action
        friend = new Friend(expectedAccountID, expectedList);

        // Assert Message
        String getConstructorMessage = "The actual AccountID does not match with expected AccountID";
        Assertions.assertEquals(expectedAccountID.getID(), friend.getAccountID(), getConstructorMessage);
    }
    // test getFriends
    @Test void testGetFriends() {
        // Expected Values
        AccountID expectedAccountID = new AccountID(null);
        expectedAccountID.generateID();

        AccountID expectedFriendAccountID1 = new AccountID(null);
        expectedFriendAccountID1.generateID();

        AccountID expectedFriendAccountID2 = new AccountID(null);
        expectedFriendAccountID2.generateID();

        AccountID expectedFriendAccountID3 = new AccountID(null);
        expectedFriendAccountID3.generateID();

        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add(expectedFriendAccountID1.getID());
        expectedList.add(expectedFriendAccountID2.getID());
        expectedList.add(expectedFriendAccountID3.getID());

        // Action
        friend = new Friend(expectedAccountID, expectedList);

        // Assert Message
        String getConstructorMessage = "The actual friends list does not match with expected friends list";
        Assertions.assertEquals(expectedList, friend.getFriends(), getConstructorMessage);
    }


}
