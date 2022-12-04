package controller;

import com.backend.QuestPetsApplication;
import com.backend.controller.FriendController;
import com.backend.entities.Friend;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.ProtectedAccount;
import com.backend.repositories.FriendsRepo;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.FriendsManager;
import net.minidev.json.JSONArray;
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
import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendControllerTest {
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;
    private final FriendController friendController;
    private final FriendsManager friendsManager;
    private final FriendsRepo friendsRepo;

    SessionID user1SessionID;
    SessionID user2SessionID;
    SessionID user3SessionID;

    @Autowired
    public FriendControllerTest(AccountSystemFacade accountSystemFacade, AccountManager accountManager, FriendController friendController, FriendsManager friendsManager, FriendsRepo friendsRepo) {
        this.accountSystemFacade = accountSystemFacade;
        this.accountManager = accountManager;
        this.friendController = friendController;
        this.friendsManager = friendsManager;
        this.friendsRepo = friendsRepo;
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
        JSONArray responseList = (JSONArray) ((JSONObject) Objects.requireNonNull(this.friendController.getFriends(user1SessionID.getID()).getBody())).get("friends");
        ArrayList<String> actualList = new ArrayList<>();

        for(Object account: responseList) {
            actualList.add(((ProtectedAccount)account).getUsername());
        }

        // Assertion Message
        String getUserFriendsMessage = "friends not found";

        // Assertion Statement
        String user2 = "dummy2";
        Assertions.assertTrue(actualList.contains(user2), getUserFriendsMessage);
        String user3 = "dummy3";
        Assertions.assertTrue(actualList.contains(user3), getUserFriendsMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
        this.friendsRepo.deleteById(user3AccountID);
    }

    @Test
    public void getFriendsInvalidSessionIDTest() {
        // Action
        String expectedResponse = "Session does not exist!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendController.getFriends("90awe89fwaef").getBody())).get("Message");

        // Assertion Message
        String getUserFriendsMessage = "friends not found";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, getUserFriendsMessage);
    }

    @Test
    public void controllerDeleteFriendTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        ArrayList<String> user1List = new ArrayList<>();
        user1List.add(user2AccountID);
        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);

        this.friendsRepo.save(new Friend(new AccountID(user1AccountID), user1List));
        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user2List));

        // First half of Action
        ArrayList<String> response1List = this.friendsManager.getFriends(user1AccountID);
        ArrayList<String> response2List = this.friendsManager.getFriends(user2AccountID);

        // Assertion Message
        String deleteFriendMessage = "User is not in the opposite's friend list";

        // First half of Assertion statement
        Assertions.assertTrue(response1List.contains(user2AccountID), deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteFriendMessage);

        // Second half of Action
        String user2 = "dummy2";
        HttpStatus responseStatus1 = this.friendController.deleteFriend(user2, user1SessionID.getID()).getStatusCode();
        response1List = this.friendsManager.getFriends(user1AccountID);
        response2List = this.friendsManager.getFriends(user2AccountID);

        // Assertion Statement
        deleteFriendMessage = "User is still in opposite's friend list";

        // Second half of Assertion Statement
        Assertions.assertEquals(HttpStatus.OK, responseStatus1, deleteFriendMessage);
        Assertions.assertFalse(response1List.contains(user2AccountID), deleteFriendMessage);
        Assertions.assertFalse(response2List.contains(user1AccountID), deleteFriendMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }

    @Test
    public void controllerDeleteFriendInvalidSessionIDTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        ArrayList<String> user1List = new ArrayList<>();
        user1List.add(user2AccountID);
        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);

        this.friendsRepo.save(new Friend(new AccountID(user1AccountID), user1List));
        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user2List));

        // First half of Action
        ArrayList<String> response1List = this.friendsManager.getFriends(user1AccountID);
        ArrayList<String> response2List = this.friendsManager.getFriends(user2AccountID);

        // Assertion Message
        String deleteFriendMessage = "User is not in the opposite's friend list";

        // First half of Assertion statement
        Assertions.assertTrue(response1List.contains(user2AccountID), deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteFriendMessage);

        // Second half of Action
        String user2 = "dummy2";
        String expectedResponse = "Session does not exist!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendController.deleteFriend(user2, "woaiejg9waf").getBody())).get("Message");
        response1List = this.friendsManager.getFriends(user1AccountID);
        response2List = this.friendsManager.getFriends(user2AccountID);

        // Assertion Statement
        deleteFriendMessage = "SessionID is valid";

        // Second half of Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, deleteFriendMessage);
        Assertions.assertTrue(response1List.contains(user2AccountID), deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteFriendMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }
    @Test
    public void controllerDeleteFriendListNoUserTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        ArrayList<String> user1List = new ArrayList<>();
        ArrayList<String> user2List = new ArrayList<>();

        this.friendsRepo.save(new Friend(new AccountID(user1AccountID), user1List));
        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user2List));

        // First half of Action
        ArrayList<String> response1List = this.friendsManager.getFriends(user1AccountID);
        ArrayList<String> response2List = this.friendsManager.getFriends(user2AccountID);

        // Assertion Message
        String deleteFriendMessage = "User is in the opposite's friend list";

        // First half of Assertion statement
        Assertions.assertFalse(response1List.contains(user2AccountID), deleteFriendMessage);
        Assertions.assertFalse(response2List.contains(user1AccountID), deleteFriendMessage);

        // Second half of Action
        String user2 = "dummy2";
        String expectedResponse = "User's friend list is empty!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendController.deleteFriend(user2, user1SessionID.getID()).getBody())).get("Message");
        response1List = this.friendsManager.getFriends(user1AccountID);
        response2List = this.friendsManager.getFriends(user2AccountID);

        // Assertion Statement
        deleteFriendMessage = "SessionID is valid";

        // Second half of Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, deleteFriendMessage);
        Assertions.assertFalse(response1List.contains(user2AccountID), deleteFriendMessage);
        Assertions.assertFalse(response2List.contains(user1AccountID), deleteFriendMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }
    @Test
    public void controllerDeleteFriendFriendNotFoundInListTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();

        ArrayList<String> user1List = new ArrayList<>();
        user1List.add(user2AccountID);
        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);
        user2List.add(user3AccountID);
        ArrayList<String> user3List = new ArrayList<>();
        user3List.add(user2AccountID);

        this.friendsRepo.save(new Friend(new AccountID(user1AccountID), user1List));
        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user2List));
        this.friendsRepo.save(new Friend(new AccountID(user3AccountID), user3List));

        // First half of Action
        ArrayList<String> response1List = this.friendsManager.getFriends(user1AccountID);
        ArrayList<String> response2List = this.friendsManager.getFriends(user2AccountID);
        ArrayList<String> response3List = this.friendsManager.getFriends(user3AccountID);

        // Assertion Message
        String deleteFriendMessage = "User is in the opposite's friend list";

        // First half of Assertion statement
        Assertions.assertTrue(response1List.contains(user2AccountID), deleteFriendMessage);
        Assertions.assertFalse(response1List.contains(user3AccountID), deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user3AccountID), deleteFriendMessage);
        Assertions.assertTrue(response3List.contains(user2AccountID), deleteFriendMessage);

        // Action
        String expectedResponse = "Friend does not exist in user's list!";
        String user3 = "dummy3";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendController.deleteFriend(user3, user1SessionID.getID()).getBody())).get("Message");
        response1List = this.friendsManager.getFriends(user1AccountID);
        response2List = this.friendsManager.getFriends(user2AccountID);
        response3List = this.friendsManager.getFriends(user3AccountID);

        // Assertion Statement
        deleteFriendMessage = "Friend exists in user's list";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, deleteFriendMessage);
        Assertions.assertTrue(response1List.contains(user2AccountID), deleteFriendMessage);
        Assertions.assertFalse(response1List.contains(user3AccountID), deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user3AccountID), deleteFriendMessage);
        Assertions.assertTrue(response3List.contains(user2AccountID), deleteFriendMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
        this.friendsRepo.deleteById(user3AccountID);
    }
    @Test
    public void controllerDeleteUserListNoFriendTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();

        ArrayList<String> user1List = new ArrayList<>();
        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);
        user2List.add(user3AccountID);
        ArrayList<String> user3List = new ArrayList<>();
        user3List.add(user2AccountID);
        user3List.add(user1AccountID);

        this.friendsRepo.save(new Friend(new AccountID(user1AccountID), user1List));
        this.friendsRepo.save(new Friend(new AccountID(user2AccountID), user2List));
        this.friendsRepo.save(new Friend(new AccountID(user3AccountID), user3List));

        // First half of Action
        ArrayList<String> response1List = this.friendsManager.getFriends(user1AccountID);
        ArrayList<String> response2List = this.friendsManager.getFriends(user2AccountID);
        ArrayList<String> response3List = this.friendsManager.getFriends(user3AccountID);

        // Assertion Message
        String deleteFriendMessage = "User is in the opposite's friend list";

        // First half of Assertion statement
        Assertions.assertFalse(response1List.contains(user2AccountID), deleteFriendMessage);
        Assertions.assertFalse(response1List.contains(user3AccountID), deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user3AccountID), deleteFriendMessage);
        Assertions.assertTrue(response3List.contains(user2AccountID), deleteFriendMessage);

        // Action
        String user1 = "dummy1";
        String expectedResponse = "User does not exist in Friend's List!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendController.deleteFriend(user1, user3SessionID.getID()).getBody())).get("Message");
        response1List = this.friendsManager.getFriends(user1AccountID);
        response2List = this.friendsManager.getFriends(user2AccountID);
        response3List = this.friendsManager.getFriends(user3AccountID);

        // Assertion Statement
        deleteFriendMessage = "User exists in Friend's list";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, deleteFriendMessage);
        Assertions.assertFalse(response1List.contains(user2AccountID), deleteFriendMessage);
        Assertions.assertFalse(response1List.contains(user3AccountID), deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user3AccountID), deleteFriendMessage);
        Assertions.assertTrue(response3List.contains(user2AccountID), deleteFriendMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
        this.friendsRepo.deleteById(user3AccountID);
    }
    @Test
    public void controllerDeleteFriendBothListDontExistTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        // First half of Action
        ArrayList<String> response1List = this.friendsManager.getFriends(user1AccountID);
        ArrayList<String> response2List = this.friendsManager.getFriends(user2AccountID);

        // Assertion Message
        String deleteFriendMessage = "User is in the opposite's friend list";

        // First half of Assertion statement
        Assertions.assertEquals(new ArrayList<>(), response1List, deleteFriendMessage);
        Assertions.assertEquals(new ArrayList<>(), response2List, deleteFriendMessage);

        // Action
        String user1 = "dummy1";
        String expectedResponse = "Both do not have Friends!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendController.deleteFriend(user1, user2SessionID.getID()).getBody())).get("Message");
        response1List = this.friendsManager.getFriends(user1AccountID);
        response2List = this.friendsManager.getFriends(user2AccountID);

        // Assertion Statement
        deleteFriendMessage = "User exists in Friend's list";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, deleteFriendMessage);
        Assertions.assertEquals(new ArrayList<>(), response1List, deleteFriendMessage);
        Assertions.assertEquals(new ArrayList<>(), response2List, deleteFriendMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }
    @Test
    public void controllerDeleteFriendUserListDontExistTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);

        this.friendsRepo.save(new Friend(user2AccountID, user2List));

        // First half of Action
        ArrayList<String> response1List = this.friendsManager.getFriends(user1AccountID);
        ArrayList<String> response2List = this.friendsManager.getFriends(user2AccountID);


        // Assertion Message
        String deleteFriendMessage = "User is in the opposite's friend list";

        // First half of Assertion statement
        Assertions.assertEquals(new ArrayList<>(), response1List, deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteFriendMessage);

        // Action
        String user2 = "dummy2";
        String expectedResponse = "User does not have friends!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendController.deleteFriend(user2, user1SessionID.getID()).getBody())).get("Message");
        response1List = this.friendsManager.getFriends(user1AccountID);
        response2List = this.friendsManager.getFriends(user2AccountID);

        // Assertion Statement
        deleteFriendMessage = "User exists in Friend's list";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, deleteFriendMessage);
        Assertions.assertEquals(new ArrayList<>(), response1List, deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteFriendMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user2AccountID);
    }
    @Test
    public void controllerDeleteFriendFriendListDontExistTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);

        this.friendsRepo.save(new Friend(user2AccountID, user2List));

        // First half of Action
        ArrayList<String> response1List = this.friendsManager.getFriends(user1AccountID);
        ArrayList<String> response2List = this.friendsManager.getFriends(user2AccountID);


        // Assertion Message
        String deleteFriendMessage = "User is in the opposite's friend list";

        // First half of Assertion statement
        Assertions.assertEquals(new ArrayList<>(), response1List, deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteFriendMessage);

        // Action
        String user1 = "dummy1";
        String expectedResponse = "Friend does not have friends!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendController.deleteFriend(user1, user2SessionID.getID()).getBody())).get("Message");
        response1List = this.friendsManager.getFriends(user1AccountID);
        response2List = this.friendsManager.getFriends(user2AccountID);

        // Assertion Statement
        deleteFriendMessage = "User exists in Friend's list";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, deleteFriendMessage);
        Assertions.assertEquals(new ArrayList<>(), response1List, deleteFriendMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteFriendMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user2AccountID);
    }

    @Test
    public void controllerDeleteAllCorrelatedFriendsSuccessTest() {
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

        // First half of Action
        ArrayList<String> response1List = this.friendsManager.getFriends(user1AccountID);
        ArrayList<String> response2List = this.friendsManager.getFriends(user2AccountID);
        ArrayList<String> response3List = this.friendsManager.getFriends(user3AccountID);

        // Assertion Message
        String deleteAllCorrelatedFriendMessage = "Does not have user1 in list";

        // First half of Assertion statement
        Assertions.assertTrue(response1List.contains(user2AccountID), deleteAllCorrelatedFriendMessage);
        Assertions.assertTrue(response1List.contains(user3AccountID), deleteAllCorrelatedFriendMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteAllCorrelatedFriendMessage);
        Assertions.assertTrue(response3List.contains(user1AccountID), deleteAllCorrelatedFriendMessage);

        // Action
        HttpStatus expectedResponse = HttpStatus.OK;
        HttpStatus actualResponse = this.friendController.deleteAllCorrelatedFriends(user1SessionID.getID()).getStatusCode();

        response1List = this.friendsManager.getFriends(user1AccountID);
        response2List = this.friendsManager.getFriends(user2AccountID);
        response3List = this.friendsManager.getFriends(user3AccountID);

        // Assertion Message
        String deleteAllCorrelatedFriendsSuccessMessage = "user1 still exists in correlated lists";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, deleteAllCorrelatedFriendsSuccessMessage);
        Assertions.assertEquals(0, response1List.size(), deleteAllCorrelatedFriendMessage);
        Assertions.assertEquals(0, response2List.size(), deleteAllCorrelatedFriendMessage);
        Assertions.assertEquals(0, response3List.size(), deleteAllCorrelatedFriendMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
        this.friendsRepo.deleteById(user3AccountID);
    }
    @Test
    public void controllerDeleteAllCorrelatedFriendsInvalidSessionIDTest() {
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

        // First half of Action
        ArrayList<String> response1List = this.friendsManager.getFriends(user1AccountID);
        ArrayList<String> response2List = this.friendsManager.getFriends(user2AccountID);
        ArrayList<String> response3List = this.friendsManager.getFriends(user3AccountID);

        // Assertion Message
        String deleteAllCorrelatedFriendMessage = "Does not have user1 in list";

        // First half of Assertion statement
        Assertions.assertTrue(response1List.contains(user2AccountID), deleteAllCorrelatedFriendMessage);
        Assertions.assertTrue(response1List.contains(user3AccountID), deleteAllCorrelatedFriendMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteAllCorrelatedFriendMessage);
        Assertions.assertTrue(response3List.contains(user1AccountID), deleteAllCorrelatedFriendMessage);

        // Action
        String expectedResponse = "Session does not exist!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendController.deleteAllCorrelatedFriends("aowie9aw9egf").getBody())).get("Message");

        response1List = this.friendsManager.getFriends(user1AccountID);
        response2List = this.friendsManager.getFriends(user2AccountID);
        response3List = this.friendsManager.getFriends(user3AccountID);

        // Assertion Message
        String deleteAllCorrelatedFriendsInvalidSessionIDMessage = "user1 still exists in correlated lists";

        Assertions.assertEquals(expectedResponse, actualResponse, deleteAllCorrelatedFriendsInvalidSessionIDMessage);
        Assertions.assertTrue(response1List.contains(user2AccountID), deleteAllCorrelatedFriendsInvalidSessionIDMessage);
        Assertions.assertTrue(response1List.contains(user3AccountID), deleteAllCorrelatedFriendsInvalidSessionIDMessage);
        Assertions.assertTrue(response2List.contains(user1AccountID), deleteAllCorrelatedFriendsInvalidSessionIDMessage);
        Assertions.assertTrue(response3List.contains(user1AccountID), deleteAllCorrelatedFriendsInvalidSessionIDMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
        this.friendsRepo.deleteById(user3AccountID);
    }


}
