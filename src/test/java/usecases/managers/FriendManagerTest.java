package usecases.managers;

import com.backend.QuestPetsApplication;
import com.backend.entities.Friend;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.repositories.FriendsRepo;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.FriendsManager;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendManagerTest {

    private final FriendsRepo friendsRepo;
    private final FriendsManager friendsManager;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;
    private SessionID user1SessionID;
    private SessionID user2SessionID;
    private SessionID user3SessionID;

    @Autowired
    public FriendManagerTest(FriendsRepo friendsRepo, FriendsManager friendsManager, AccountSystemFacade accountSystemFacade, AccountManager accountManager) {
        this.friendsRepo = friendsRepo;
        this.friendsManager = friendsManager;
        this.accountSystemFacade = accountSystemFacade;
        this.accountManager = accountManager;
    }

    @BeforeEach
    public void setup() {
        String user1 = "dummy1";
        String user2 = "dummy2";
        String user3 = "dummy3";
        String password = "abc123!";

        ResponseEntity<Object> register1 = this.accountSystemFacade.registerAccount(user1, password);
        ResponseEntity<Object> register2 = this.accountSystemFacade.registerAccount(user2, password);
        ResponseEntity<Object> register3 = this.accountSystemFacade.registerAccount(user3, password);


        if (!(register1.getStatusCode() == HttpStatus.OK)){
            user1SessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(user1, password).getBody())).get("sessionID"));
        } else  {
            user1SessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(register1.getBody())).get("sessionID"));
        }

        if (!(register2.getStatusCode() == HttpStatus.OK)){
            user2SessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(user2, password).getBody())).get("sessionID"));
        } else  {
            user2SessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(register2.getBody())).get("sessionID"));
        }

        if (!(register3.getStatusCode() == HttpStatus.OK)){
            user3SessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(user3, password).getBody())).get("sessionID"));
        } else  {
            user3SessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(register3.getBody())).get("sessionID"));
        }

    }

    @AfterEach
    public void tearDown() {
        if (user1SessionID != null) {
            this.accountSystemFacade.logoutAccount(user1SessionID);
        }
        if (user2SessionID != null) {
            this.accountSystemFacade.logoutAccount(user2SessionID);
        }
        if (user3SessionID != null) {
            this.accountSystemFacade.logoutAccount(user3SessionID);
        }
    }

    @Test
    public void getFriendsTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        ArrayList<String> user1List = new ArrayList<>();
        user1List.add(user2AccountID);
        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);

        this.friendsRepo.save(new Friend(new AccountID(user1AccountID), user1List));
        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user2List));

        // Action
        ArrayList<String> actualList = this.friendsManager.getFriends(user1AccountID);

        // Assertion Message
        String getFriendsMessage = String.format("The given friend ID '%s' has not been found in user's friend list", user2AccountID);

        // Assertion Statement
        Assertions.assertTrue(actualList.contains(user2AccountID), getFriendsMessage);

        // Custom CleanUp
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }

    @Test
    public void getFriendsEmptyTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();

        ArrayList<String> user1List = new ArrayList<>();

        this.friendsRepo.save(new Friend(new AccountID(user1AccountID), user1List));

        // Action
        ArrayList<String> actualList = this.friendsManager.getFriends(user1AccountID);

        // Assertion Message
        String getFriendsMessage = "The user's friend list is not empty";

        // Assertion Statement
        Assertions.assertEquals(0, actualList.size(), getFriendsMessage);
        // Custom CleanUp
        this.friendsRepo.deleteById(user1AccountID);
    }

    @Test
    public void areFriendsTrueTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        ArrayList<String> user1List = new ArrayList<>();
        user1List.add(user2AccountID);
        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);

        this.friendsRepo.save(new Friend(new AccountID(user1AccountID), user1List));
        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user2List));

        // Action
        boolean actualVal = this.friendsManager.areFriends(user1AccountID, user2AccountID);

        // AssertionMessage
        String areFriendsTestMessage = "User 1 and 2 are not friends";

        Assertions.assertTrue(actualVal, areFriendsTestMessage);

        // Custom CleanUp
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }

    @Test
    public void areFriendsFalseTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();


        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);

        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user2List));

        // Action
        boolean actualVal = this.friendsManager.areFriends(user1AccountID, user2AccountID);

        // AssertionMessage
        String areFriendsTestMessage = "User 1 does exists in User2's list";

        Assertions.assertFalse(actualVal, areFriendsTestMessage);

        // Custom CleanUp
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }

    @Test
    public void areFriendsInvalidTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);

        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user2List));

        // Action
        boolean actualVal = this.friendsManager.areFriends(user1AccountID, user2AccountID);

        // AssertionMessage
        String areFriendsTestMessage = "User 1 is present!";

        Assertions.assertFalse(actualVal, areFriendsTestMessage);

        // Custom CleanUp
        this.friendsRepo.deleteById(user2AccountID);
    }
    @Test
    public void addFriendSuccessTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        // Action
        ResponseEntity<Object> response = this.friendsManager.addFriend(user1AccountID, user2AccountID);

        // Assertion Message
        String addFriendSuccessTest = "Cannot add friend";

        // Assertion Statement
        Assertions.assertSame(response.getStatusCode(), HttpStatus.OK, addFriendSuccessTest);

        // Custom CleanUp
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }
    @Test
    public void addFriendAlreadyExistsTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        ArrayList<String> user1List = new ArrayList<>();
        user1List.add(user2AccountID);
        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);

        this.friendsRepo.save(new Friend(new AccountID(user1AccountID), user1List));
        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user2List));

        // Action
        ResponseEntity<Object> response = this.friendsManager.addFriend(user1AccountID, user2AccountID);

        // Assertion Message
        String addFriendAlreadyExistsMessage = "User 1 and 2 are not friends";

        // Assertion Statement
        Assertions.assertSame(response.getStatusCode(), HttpStatus.CONFLICT, addFriendAlreadyExistsMessage);

        // Custom CleanUp
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }

    @Test
    public void userExistsTrueTest() {
        // Values
        String accountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        ArrayList<String> user1List = new ArrayList<>();

        this.friendsRepo.save(new Friend(new AccountID(accountID), user1List));

        // Action
        boolean actualResult = this.friendsManager.userExists(accountID);

        // Assertion Message
        String userExistsTrueMessage = "account does not exist in FriendDB";

        // Assertion Statement
        Assertions.assertTrue(actualResult, userExistsTrueMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(accountID);
    }

    @Test
    public void userExistsFalseTest() {
        // Values
        String accountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();

        // Action
        boolean actualResult = this.friendsManager.userExists(accountID);

        // Assertion Message
        String userExistsTrueMessage = "account exist in FriendDB";

        // Assertion Statement
        Assertions.assertFalse(actualResult, userExistsTrueMessage);
    }
    @Test
    public void updateFriendsListTest() {
        // Value
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        ArrayList<String> user1List = new ArrayList<>();

        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user1List));

        int previousSize = this.friendsManager.getFriends(user1AccountID).size();

        Assertions.assertEquals(previousSize, 0);

        user1List.add(user2AccountID);

        // Assertion Message
        String userExistsTrueMessage = "friend list not updated correctly";

        // Action
        this.friendsManager.updateFriendsList(new Friend(user1AccountID, user1List));

        int updatedSize = this.friendsManager.getFriends(user1AccountID).size();

        // Assertion Statement
        Assertions.assertEquals(updatedSize, 1, userExistsTrueMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }
    @Test
    public void deleteFriendByIDTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        ArrayList<String> user1List = new ArrayList<>();
        user1List.add(user2AccountID);
        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);

        this.friendsRepo.save(new Friend(new AccountID(user1AccountID), user1List));
        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user2List));

        // Action
        this.friendsManager.deleteFriendByID(user1AccountID);

        // Assertion Message
        String deleteFriendByIDMessage = "User exists in Friends Collection";

        // Assertion Statement
        Assertions.assertEquals(this.friendsManager.getFriends(user1AccountID).size(), 0, deleteFriendByIDMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }
    @Test
    public void getAllContainingUserIDTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();

        ArrayList<String> user1List = new ArrayList<>();
        user1List.add(user2AccountID);
        user1List.add(user3AccountID);
        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);
        ArrayList<String> user3List = new ArrayList<>();
        user3List.add(user1AccountID);

        this.friendsRepo.save(new Friend(new AccountID(user1AccountID), user1List));
        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user2List));
        this.friendsRepo.save(new Friend(new AccountID(user3AccountID), user3List));

        // Action
        List<Friend> friendsContainingUserID = friendsManager.getAllContainingUserID(user1AccountID);
        ArrayList<String> actualList = new ArrayList<>();

        for(Friend friend : friendsContainingUserID) {
            actualList.add(friend.getAccountID());
        }
        // Assertion Message
        String getAllContainingUserIDMessage = "User does not exist in Friend's Account";

        // Assertion Statement
        Assertions.assertTrue(actualList.contains(user2AccountID) && actualList.contains(user3AccountID), getAllContainingUserIDMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
        this.friendsRepo.deleteById(user3AccountID);
    }

    @Test
    public void getAllContainingUserIDNoFriendsTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();

        ArrayList<String> user1List = new ArrayList<>();
        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user3AccountID);
        ArrayList<String> user3List = new ArrayList<>();
        user3List.add(user2AccountID);

        this.friendsRepo.save(new Friend(new AccountID(user1AccountID), user1List));
        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user2List));
        this.friendsRepo.save(new Friend(new AccountID(user3AccountID), user3List));

        // Action
        List<Friend> friendsContainingUserID = friendsManager.getAllContainingUserID(user1AccountID);
        ArrayList<String> actualList = new ArrayList<>();

        for(Friend friend : friendsContainingUserID) {
            actualList.add(friend.getAccountID());
        }
        // Assertion Message
        String getAllContainingUserIDNoFriendsMessage = "User does exist in Friend's Account";

        // Assertion Statement
        Assertions.assertTrue(!actualList.contains(user2AccountID) && !actualList.contains(user3AccountID), getAllContainingUserIDNoFriendsMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
        this.friendsRepo.deleteById(user3AccountID);
    }
}
