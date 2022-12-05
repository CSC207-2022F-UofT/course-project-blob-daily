package usecases.facades;

import com.backend.QuestPetsApplication;
import com.backend.entities.Friend;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Invitation;
import com.backend.entities.users.ProtectedAccount;
import com.backend.repositories.FriendsRepo;
import com.backend.repositories.InvitationsRepo;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.facades.FriendSystemFacade;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.FriendsManager;
import com.backend.usecases.managers.InvitationsManager;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendSystemFacadeTest {
    private final AccountManager accountManager;
    private final AccountSystemFacade accountSystemFacade;
    private final FriendsManager friendsManager;
    private final FriendsRepo friendsRepo;
    private final FriendSystemFacade friendSystemFacade;
    private final InvitationsRepo invitationsRepo;
    private final InvitationsManager invitationsManager;
    private SessionID user1SessionID;
    private SessionID user2SessionID;
    private SessionID user3SessionID;
    private final String user1 = "dummy1";
    private final String user2 = "dummy2";

    @Autowired
    public FriendSystemFacadeTest(AccountManager accountManager, AccountSystemFacade accountSystemFacade, FriendsManager friendsManager, FriendsRepo friendsRepo, FriendSystemFacade friendSystemFacade, InvitationsRepo invitationsRepo, InvitationsManager invitationsManager) {
        this.accountManager = accountManager;
        this.accountSystemFacade = accountSystemFacade;
        this.friendsManager = friendsManager;
        this.friendsRepo = friendsRepo;
        this.friendSystemFacade = friendSystemFacade;
        this.invitationsRepo = invitationsRepo;
        this.invitationsManager = invitationsManager;
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
    public void verifySessionAndInvitationTest() {
        // Value
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        Date currentTime = new Date(System.currentTimeMillis());

        Invitation actualInvitation = new Invitation(user1AccountID + user2AccountID, user1AccountID, user2AccountID, currentTime);
        this.invitationsRepo.save(actualInvitation);
        HttpStatus expectedResponse = HttpStatus.OK;
        // Action
        HttpStatus actualResponse = this.friendSystemFacade.verifySessionAndInvitation(user2, user1SessionID.getID()).getStatusCode();

        // Assertion Message
        String verifySessionAndInvitationMessage = "Invalid Session and Invitation";

        Assertions.assertEquals(expectedResponse, actualResponse, verifySessionAndInvitationMessage);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID + user2AccountID);
    }
    @Test
    public void verifySessionAndInvitationInvalidSessionIDTest() {
        // Value
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        Date currentTime = new Date(System.currentTimeMillis());

        Invitation actualInvitation = new Invitation(user1AccountID + user2AccountID, user1AccountID, user2AccountID, currentTime);
        this.invitationsRepo.save(actualInvitation);
        HttpStatus expectedResponse = HttpStatus.NOT_FOUND;
        // Action
        HttpStatus actualResponse = this.friendSystemFacade.verifySessionAndInvitation(user2, "oiawjefjew").getStatusCode();

        // Assertion Message
        String verifySessionAndInvitationMessage = "Valid SessionID";

        Assertions.assertEquals(expectedResponse, actualResponse, verifySessionAndInvitationMessage);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID + user2AccountID);
    }
    @Test
    public void getUserFriendsTest() {
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
        JSONArray responseList = (JSONArray) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.getUserFriends(user1SessionID.getID()).getBody())).get("friends");
        ArrayList<String> actualList = new ArrayList<>();

        for(Object account: responseList) {
            actualList.add(((ProtectedAccount)account).getUsername());
        }

        // Assertion Message
        String getUserFriendsMessage = "friends not found";

        // Assertion Statement
        Assertions.assertTrue(actualList.contains(user2), getUserFriendsMessage);
        String user3 = "dummy3";
        Assertions.assertTrue(actualList.contains(user3), getUserFriendsMessage);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
        this.friendsRepo.deleteById(user3AccountID);
    }
    @Test
    public void getUserFriendsInvalidSessionIDTest() {
        // Action
        String expectedResponse = "Session does not exist!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.getUserFriends("90awe89fwaef").getBody())).get("Message");

        // Assertion Message
        String getUserFriendsMessage = "friends not found";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, getUserFriendsMessage);

    }
    @Test
    public void deleteFriendTest() {
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
        HttpStatus responseStatus1 = this.friendSystemFacade.deleteFriend(user2, user1SessionID.getID()).getStatusCode();
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
    public void deleteFriendInvalidSessionIDTest() {
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
        String expectedResponse = "Session does not exist!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.deleteFriend(user2, "woaiejg9waf").getBody())).get("Message");
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
    public void deleteFriendListNoUserTest() {
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
        String expectedResponse = "User's friend list is empty!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.deleteFriend(user2, user1SessionID.getID()).getBody())).get("Message");
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
    public void deleteFriendFriendNotFoundInListTest() {
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
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.deleteFriend(user3, user1SessionID.getID()).getBody())).get("Message");
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
    public void deleteUserListNoFriendTest() {
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
        String expectedResponse = "User does not exist in Friend's List!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.deleteFriend(user1, user3SessionID.getID()).getBody())).get("Message");
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
    public void deleteFriendBothListDontExistTest() {
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
        String expectedResponse = "Both do not have Friends!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.deleteFriend(user1, user2SessionID.getID()).getBody())).get("Message");
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
    public void deleteFriendUserListDontExistTest() {
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
        String expectedResponse = "User does not have friends!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.deleteFriend(user2, user1SessionID.getID()).getBody())).get("Message");
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
    public void deleteFriendFriendListDontExistTest() {
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
        String expectedResponse = "Friend does not have friends!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.deleteFriend(user1, user2SessionID.getID()).getBody())).get("Message");
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
    public void deleteAllCorrelatedFriendsSuccessTest() {
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
        HttpStatus actualResponse = this.friendSystemFacade.deleteAllCorrelatedFriends(user1SessionID.getID()).getStatusCode();

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
    public void deleteAllCorrelatedFriendsInvalidSessionIDTest() {
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
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.deleteAllCorrelatedFriends("aowie9aw9egf").getBody())).get("Message");

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
    @Test
    public void getInvitationsSuccessTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));
        this.invitationsRepo.save(new Invitation(new AccountID(user3AccountID), new AccountID(user1AccountID), timestamp));

        // Action
        JSONArray responseReceiverList = (JSONArray) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.getInvitations(user1SessionID.getID(), true).getBody())).get("invites");
        JSONArray responseSenderList = (JSONArray) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.getInvitations(user1SessionID.getID(), false).getBody())).get("invites");

        ArrayList<String> actualReceiverList = new ArrayList<>();
        ArrayList<String> actualSenderList = new ArrayList<>();

        for(Object invitation : responseReceiverList) {
            actualReceiverList.add(((ProtectedAccount)invitation).getUsername());
        }
        for(Object invitation : responseSenderList) {
            actualSenderList.add(((ProtectedAccount)invitation).getUsername());
        }

        // Assertion Statement
        Assertions.assertTrue(actualReceiverList.contains("dummy3"));
        Assertions.assertTrue(actualSenderList.contains("dummy2"));

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID+user2AccountID);
        this.invitationsRepo.deleteById(user3AccountID+user1AccountID);

    }
    @Test
    public void getInvitationInvalidSessionIDTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));
        this.invitationsRepo.save(new Invitation(new AccountID(user3AccountID), new AccountID(user1AccountID), timestamp));

        // Action
        HttpStatus expectedResponse = HttpStatus.INTERNAL_SERVER_ERROR;
        HttpStatus actualReceiverResponse = this.friendSystemFacade.getInvitations("oaiwejg2", true).getStatusCode();
        HttpStatus actualSenderResponse = this.friendSystemFacade.getInvitations("oaiwejg2", false).getStatusCode();


        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualReceiverResponse);
        Assertions.assertEquals(expectedResponse, actualSenderResponse);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID+user2AccountID);
        this.invitationsRepo.deleteById(user3AccountID+user1AccountID);
    }
    @Test
    public void sendInvitationSuccessTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        // Action
        String expectedResponse = "Invitation successfully sent!";
        String actualResponse = (String)this.friendSystemFacade.sendInvitation("dummy2", user1SessionID.getID()).getBody();
        Invitation actualInvitation = this.invitationsRepo.findBySenderIDAndReceiverID(user1AccountID, user2AccountID);

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse);
        Assertions.assertEquals(user1AccountID, actualInvitation.getSenderID());
        Assertions.assertEquals(user2AccountID, actualInvitation.getReceiverID());

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID+user2AccountID);
    }
    @Test
    public void sendInvitationAlreadySentTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));

        // Action
        String expectedResponse = "Invitation already sent!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.sendInvitation("dummy2", user1SessionID.getID()).getBody())).get("Message");
        Invitation actualInvitation = this.invitationsRepo.findBySenderIDAndReceiverID(user1AccountID, user2AccountID);

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse);
        Assertions.assertEquals(user1AccountID, actualInvitation.getSenderID());
        Assertions.assertEquals(user2AccountID, actualInvitation.getReceiverID());

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID+user2AccountID);
    }
    @Test
    public void sendInvitationAutomaticallyAcceptedTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user2AccountID), new AccountID(user1AccountID), timestamp));

        // Action
        String expectedResponse = "Friend request automatically accepted!";
        String actualResponse = (String)this.friendSystemFacade.sendInvitation("dummy2", user1SessionID.getID()).getBody();
        Invitation actualInvitation = this.invitationsRepo.findBySenderIDAndReceiverID(user1AccountID, user2AccountID);

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse);
        Assertions.assertNull(actualInvitation);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }
    @Test
    public void sendInvitationAlreadyFriendsTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        ArrayList<String> user1List = new ArrayList<>();
        user1List.add(user2AccountID);
        ArrayList<String> user2List = new ArrayList<>();
        user2List.add(user1AccountID);

        this.friendsRepo.save(new Friend(user1AccountID, user1List));
        this.friendsRepo.save(new Friend(user2AccountID, user2List));

        // Action
        String expectedResponse = "Users are already friends!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.sendInvitation("dummy2", user1SessionID.getID()).getBody())).get("Message");
        Invitation actualInvitation = this.invitationsRepo.findBySenderIDAndReceiverID(user1AccountID, user2AccountID);

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse);
        Assertions.assertNull(actualInvitation);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID+user2AccountID);
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }
    @Test
    public void withdrawInvitationSuccessTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));

        // Action
        String expectedResponse = "Invitation successfully deleted!";
        String actualResponse = (String) this.friendSystemFacade.withdrawInvitation("dummy2", user1SessionID.getID()).getBody();

        // Assertion Message
        String withdrawInvitationMessage = "Invitation unsuccessfully deleted";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, withdrawInvitationMessage);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID+user2AccountID);
    }
    @Test
    public void withdrawInvitationDNETest() {
        // Action
        String expectedResponse = "Invitation does not exist!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.withdrawInvitation("dummy2", user1SessionID.getID()).getBody())).get("Message");

        // Assertion Message
        String withdrawInvitationMessage = "Invitation unsuccessfully deleted";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, withdrawInvitationMessage);
    }
    @Test
    public void handleInvitationAcceptedTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));

        // Action
        String expectedResponse = "invitation successfully accepted!";
        String actualResponse = (String) this.friendSystemFacade.handleInvitation("dummy1", user2SessionID.getID(), true).getBody();
        Invitation actualInvitation = this.invitationsRepo.findBySenderIDAndReceiverID(user1AccountID, user2AccountID);

        ArrayList<String> user1Friend = this.friendsManager.getFriends(user1AccountID);
        ArrayList<String> user2Friend = this.friendsManager.getFriends(user2AccountID);

        // Assertion Message
        String handInvitationAcceptedMessage = "invitation has not been accepted";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, handInvitationAcceptedMessage);
        Assertions.assertNull(actualInvitation);
        Assertions.assertTrue(user1Friend.contains(user2AccountID));
        Assertions.assertTrue(user2Friend.contains(user1AccountID));

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }
    @Test
    public void handleInvitationDeclinedTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));

        // Action
        String expectedResponse = "invitation successfully declined!";
        String actualResponse = (String) this.friendSystemFacade.handleInvitation("dummy1", user2SessionID.getID(), false).getBody();
        Invitation actualInvitation = this.invitationsRepo.findBySenderIDAndReceiverID(user1AccountID, user2AccountID);

        ArrayList<String> user1Friend = this.friendsManager.getFriends(user1AccountID);
        ArrayList<String> user2Friend = this.friendsManager.getFriends(user2AccountID);

        // Assertion Message
        String handInvitationAcceptedMessage = "invitation has not been accepted";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, handInvitationAcceptedMessage);
        Assertions.assertNull(actualInvitation);
        Assertions.assertFalse(user1Friend.contains(user2AccountID));
        Assertions.assertFalse(user2Friend.contains(user1AccountID));
    }
    @Test
    public void deleteAllCorrelatedInvitationsSuccessTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));
        this.invitationsRepo.save(new Invitation(new AccountID(user3AccountID), new AccountID(user1AccountID), timestamp));

        // First half of Action
        ArrayList<String> result1 = new ArrayList<>();
        ArrayList<String> result2 = new ArrayList<>();

        List<Invitation> response1List = this.invitationsManager.getAllByReceiverID(user1AccountID);
        List<Invitation> response2List = this.invitationsManager.getAllBySenderID(user1AccountID);

        for(Invitation invitation : response1List) {
            result1.add(invitation.getSenderID());
        }

        for(Invitation invitation : response2List) {
            result2.add(invitation.getReceiverID());
        }

        // First half of Assertion
        Assertions.assertTrue(result1.contains(user3AccountID));
        Assertions.assertTrue(result2.contains(user2AccountID));

        // Action
        String expectedResponse = "Correlated Invitations all successfully deleted!";
        String actualResponse = (String) this.friendSystemFacade.deleteAllCorrelatedInvitations(user1SessionID.getID()).getBody();

        response1List = this.invitationsManager.getAllByReceiverID(user1AccountID);
        response2List = this.invitationsManager.getAllBySenderID(user1AccountID);

        // Second half of Assertions
        Assertions.assertEquals(expectedResponse, actualResponse);
        Assertions.assertEquals(new ArrayList<>(), response1List);
        Assertions.assertEquals(new ArrayList<>(), response2List);
    }
    @Test
    public void deleteAllCorrelatedInvitationsInvalidSessionID() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));
        this.invitationsRepo.save(new Invitation(new AccountID(user3AccountID), new AccountID(user1AccountID), timestamp));

        // First half of Action
        ArrayList<String> result1 = new ArrayList<>();
        ArrayList<String> result2 = new ArrayList<>();

        List<Invitation> response1List = this.invitationsManager.getAllByReceiverID(user1AccountID);
        List<Invitation> response2List = this.invitationsManager.getAllBySenderID(user1AccountID);

        for(Invitation invitation : response1List) {
            result1.add(invitation.getSenderID());
        }

        for(Invitation invitation : response2List) {
            result2.add(invitation.getReceiverID());
        }

        // First half of Assertion
        Assertions.assertTrue(result1.contains(user3AccountID));
        Assertions.assertTrue(result2.contains(user2AccountID));

        // Action
        String expectedResponse = "Invalid SessionID!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.deleteAllCorrelatedInvitations("foaiweg0aw9eg").getBody())).get("Message");

        response1List = this.invitationsManager.getAllByReceiverID(user1AccountID);
        response2List = this.invitationsManager.getAllBySenderID(user1AccountID);

        for(Invitation invitation : response1List) {
            result1.add(invitation.getSenderID());
        }

        for(Invitation invitation : response2List) {
            result2.add(invitation.getReceiverID());
        }

        // Second half of Assertions
        Assertions.assertEquals(expectedResponse, actualResponse);
        Assertions.assertTrue(result1.contains(user3AccountID));
        Assertions.assertTrue(result2.contains(user2AccountID));

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID + user2AccountID);
        this.invitationsRepo.deleteById(user3AccountID + user1AccountID);
    }
    @Test
    public void deleteAllCorrelatedInvitationsNoInvitationsTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();


        // First half of Action
        ArrayList<String> result1 = new ArrayList<>();
        ArrayList<String> result2 = new ArrayList<>();

        List<Invitation> response1List = this.invitationsManager.getAllByReceiverID(user1AccountID);
        List<Invitation> response2List = this.invitationsManager.getAllBySenderID(user1AccountID);

        for(Invitation invitation : response1List) {
            result1.add(invitation.getSenderID());
        }

        for(Invitation invitation : response2List) {
            result2.add(invitation.getReceiverID());
        }

        // First half of Assertion
        Assertions.assertEquals(0, result1.size());
        Assertions.assertEquals(0, result2.size());

        // Action
        String expectedResponse = "No Invitations correlated to the user!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.friendSystemFacade.deleteAllCorrelatedInvitations(user1SessionID.getID()).getBody())).get("Message");

        response1List = this.invitationsManager.getAllByReceiverID(user1AccountID);
        response2List = this.invitationsManager.getAllBySenderID(user1AccountID);

        for(Invitation invitation : response1List) {
            result1.add(invitation.getSenderID());
        }

        for(Invitation invitation : response2List) {
            result2.add(invitation.getReceiverID());
        }

        // Second half of Assertions
        Assertions.assertEquals(expectedResponse, actualResponse);
        Assertions.assertEquals(0, result1.size());
        Assertions.assertEquals(0, result2.size());

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID + user2AccountID);
        this.invitationsRepo.deleteById(user3AccountID + user1AccountID);

    }
}
