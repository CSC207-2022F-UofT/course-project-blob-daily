package controller;

import com.backend.QuestPetsApplication;
import com.backend.controller.InvitationController;
import com.backend.entities.Friend;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Invitation;
import com.backend.entities.users.ProtectedAccount;
import com.backend.repositories.FriendsRepo;
import com.backend.repositories.InvitationsRepo;
import com.backend.usecases.facades.AccountSystemFacade;
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
public class InvitationControllerTest {
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;
    private final InvitationController invitationController;
    private final InvitationsManager invitationsManager;
    private final InvitationsRepo invitationsRepo;

    private final FriendsRepo friendsRepo;

    SessionID user1SessionID;
    SessionID user2SessionID;
    SessionID user3SessionID;
    private final FriendsManager friendsManager;

    @Autowired
    public InvitationControllerTest(AccountSystemFacade accountSystemFacade, AccountManager accountManager, InvitationController invitationController, InvitationsManager invitationsManager, InvitationsRepo invitationsRepo, FriendsRepo friendsRepo, FriendsManager friendsManager) {
        this.accountSystemFacade = accountSystemFacade;
        this.accountManager = accountManager;
        this.invitationController = invitationController;
        this.invitationsManager = invitationsManager;
        this.invitationsRepo = invitationsRepo;
        this.friendsRepo = friendsRepo;
        this.friendsManager = friendsManager;
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
    public void controllerGetInvitationsSuccessTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));
        this.invitationsRepo.save(new Invitation(new AccountID(user3AccountID), new AccountID(user1AccountID), timestamp));

        // Action
        JSONArray responseReceiverList = (JSONArray) ((JSONObject) Objects.requireNonNull(this.invitationController.getInvitationsAsReceiver(user1SessionID.getID()).getBody())).get("invites");
        JSONArray responseSenderList = (JSONArray) ((JSONObject) Objects.requireNonNull(this.invitationController.getInvitationsAsSender(user1SessionID.getID()).getBody())).get("invites");

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
    public void controllerGetInvitationInvalidSessionIDTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));
        this.invitationsRepo.save(new Invitation(new AccountID(user3AccountID), new AccountID(user1AccountID), timestamp));

        // Action
        HttpStatus expectedResponse = HttpStatus.INTERNAL_SERVER_ERROR;
        HttpStatus actualReceiverResponse = this.invitationController.getInvitationsAsReceiver("oaiwejg2").getStatusCode();
        HttpStatus actualSenderResponse = this.invitationController.getInvitationsAsSender("oaiwejg2").getStatusCode();


        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualReceiverResponse);
        Assertions.assertEquals(expectedResponse, actualSenderResponse);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID+user2AccountID);
        this.invitationsRepo.deleteById(user3AccountID+user1AccountID);
    }

    @Test
    public void controllerSendInvitationSuccessTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        // Action
        String expectedResponse = "Invitation successfully sent!";
        String actualResponse = (String)this.invitationController.sendInvitation("dummy2", user1SessionID.getID()).getBody();
        Invitation actualInvitation = this.invitationsRepo.findBySenderIDAndReceiverID(user1AccountID, user2AccountID);

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse);
        Assertions.assertEquals(user1AccountID, actualInvitation.getSenderID());
        Assertions.assertEquals(user2AccountID, actualInvitation.getReceiverID());

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID+user2AccountID);
    }
    @Test
    public void controllerSendInvitationAlreadySentTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));

        // Action
        String expectedResponse = "Invitation already sent!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.invitationController.sendInvitation("dummy2", user1SessionID.getID()).getBody())).get("Message");
        Invitation actualInvitation = this.invitationsRepo.findBySenderIDAndReceiverID(user1AccountID, user2AccountID);

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse);
        Assertions.assertEquals(user1AccountID, actualInvitation.getSenderID());
        Assertions.assertEquals(user2AccountID, actualInvitation.getReceiverID());

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID+user2AccountID);
    }
    @Test
    public void controllerSendInvitationAutomaticallyAcceptedTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user2AccountID), new AccountID(user1AccountID), timestamp));

        // Action
        String expectedResponse = "Friend request automatically accepted!";
        String actualResponse = (String)this.invitationController.sendInvitation("dummy2", user1SessionID.getID()).getBody();
        Invitation actualInvitation = this.invitationsRepo.findBySenderIDAndReceiverID(user1AccountID, user2AccountID);

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse);
        Assertions.assertNull(actualInvitation);

        // Custom Cleanup
        this.friendsRepo.deleteById(user1AccountID);
        this.friendsRepo.deleteById(user2AccountID);
    }
    @Test
    public void controllerSendInvitationAlreadyFriendsTest() {
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
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.invitationController.sendInvitation("dummy2", user1SessionID.getID()).getBody())).get("Message");
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
    public void controllerWithdrawInvitationSuccessTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));

        // Action
        String expectedResponse = "Invitation successfully deleted!";
        String actualResponse = (String) this.invitationController.withdrawInvitation("dummy2", user1SessionID.getID()).getBody();

        // Assertion Message
        String withdrawInvitationMessage = "Invitation unsuccessfully deleted";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, withdrawInvitationMessage);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID+user2AccountID);
    }
    @Test
    public void controllerWithdrawInvitationDNETest() {
        // Action
        String expectedResponse = "Invitation does not exist!";
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.invitationController.withdrawInvitation("dummy2", user1SessionID.getID()).getBody())).get("Message");

        // Assertion Message
        String withdrawInvitationMessage = "Invitation unsuccessfully deleted";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse, actualResponse, withdrawInvitationMessage);
    }

    @Test
    public void controllerHandleInvitationAcceptedTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));

        // Action
        String expectedResponse = "invitation successfully accepted!";
        String actualResponse = (String) this.invitationController.acceptInvitation("dummy1", user2SessionID.getID()).getBody();
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
    public void controllerHandleInvitationDeclinedTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(new AccountID(user1AccountID), new AccountID(user2AccountID), timestamp));

        // Action
        String expectedResponse = "invitation successfully declined!";
        String actualResponse = (String) this.invitationController.declineInvitation("dummy1", user2SessionID.getID()).getBody();
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
    public void controllerDeleteAllCorrelatedInvitationsSuccessTest() {
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
        String actualResponse = (String) this.invitationController.deleteAllCorrelatedInvitations(user1SessionID.getID()).getBody();

        response1List = this.invitationsManager.getAllByReceiverID(user1AccountID);
        response2List = this.invitationsManager.getAllBySenderID(user1AccountID);

        // Second half of Assertions
        Assertions.assertEquals(expectedResponse, actualResponse);
        Assertions.assertEquals(new ArrayList<>(), response1List);
        Assertions.assertEquals(new ArrayList<>(), response2List);
    }
    @Test
    public void controllerDeleteAllCorrelatedInvitationsInvalidSessionID() {
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
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.invitationController.deleteAllCorrelatedInvitations("foaiweg0aw9eg").getBody())).get("Message");

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
    public void controllerDeleteAllCorrelatedInvitationsNoInvitationsTest() {
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
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.invitationController.deleteAllCorrelatedInvitations(user1SessionID.getID()).getBody())).get("Message");

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
