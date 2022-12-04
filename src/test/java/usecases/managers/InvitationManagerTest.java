package usecases.managers;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Invitation;
import com.backend.repositories.InvitationsRepo;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.InvitationsManager;
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
public class InvitationManagerTest {
    private final InvitationsRepo invitationsRepo;
    private final InvitationsManager invitationsManager;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;
    private SessionID user1SessionID;
    private SessionID user2SessionID;
    private SessionID user3SessionID;

    @Autowired
    public InvitationManagerTest(InvitationsRepo invitationsRepo, InvitationsManager invitationsManager, AccountSystemFacade accountSystemFacade, AccountManager accountManager) {
        this.invitationsRepo = invitationsRepo;
        this.invitationsManager = invitationsManager;
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
    public void invitationExistsTrue() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        Date currentTime = new Date(System.currentTimeMillis());

        Invitation actualInvitation = new Invitation(user1AccountID + user2AccountID, user1AccountID, user2AccountID, currentTime);
        this.invitationsRepo.save(actualInvitation);

        // Action
        boolean actualValue = this.invitationsManager.invitationExists(user1AccountID, user2AccountID);

        // Assertion Message
        String invitationExistsTrueMessage = "Corresponding Invitation does not exist";

        // Assertion Statement
        Assertions.assertTrue(actualValue, invitationExistsTrueMessage);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID + user2AccountID);
    }
    @Test
    public void invitationExistsFalse() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        Date currentTime = new Date(System.currentTimeMillis());

        Invitation actualInvitation = new Invitation(user2AccountID + user1AccountID, user2AccountID, user1AccountID, currentTime);
        this.invitationsRepo.save(actualInvitation);

        // Action
        boolean actualValue = this.invitationsManager.invitationExists(user1AccountID, user2AccountID);

        // Assertion Message
        String invitationExistsFalseMessage = "Corresponding Invitation exist";

        // Assertion Statement
        Assertions.assertFalse(actualValue, invitationExistsFalseMessage);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user2AccountID + user1AccountID);

    }
    @Test
    public void checkUsersExistValidTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        // Action
        boolean actualValue = this.invitationsManager.checkUsersExist(user1AccountID, user2AccountID) == null;

        // AssertionMessage
        String checkUsersExistValidTestMessage = "Sender and Receiver does not exist";

        // Assertion Statement
        Assertions.assertTrue(actualValue, checkUsersExistValidTestMessage);
    }
    @Test
    public void checkUsersExistInvalidSenderAndReceiverTest() {
        // Values

        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.invitationsManager.checkUsersExist(null, null).getBody())).get("Message");
        String expectedResponse = "Sender and receiver does not exist!";

        // AssertionMessage
        String checkUsersExistValidTestMessage = "Sender and Receiver does exist";

        // Assertions Statement
        Assertions.assertEquals(actualResponse, expectedResponse, checkUsersExistValidTestMessage);
    }
    @Test
    public void checkUsersExistInvalidSenderTest() {
        // Values
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        // Action
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.invitationsManager.checkUsersExist(null, user2AccountID).getBody())).get("Message");
        String expectedResponse = "Sender does not exist!";

        // Assertion Message
        String checkUsersExistInvalidSenderMessage = "Sender or Receiver exists";

        // Assertion Statement
        Assertions.assertEquals(actualResponse, expectedResponse, checkUsersExistInvalidSenderMessage);
    }
    @Test
    public void checkUsersExistInvalidReceiverTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();

        // Action
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.invitationsManager.checkUsersExist(user1AccountID, null).getBody())).get("Message");
        String expectedResponse = "Receiver does not exist!";

        // Assertion Message
        String checkUsersExistInvalidSenderMessage = "Sender or Receiver exists";

        // Assertion Statement
        Assertions.assertEquals(actualResponse, expectedResponse, checkUsersExistInvalidSenderMessage);

    }
    @Test
    public void verifyInvitationTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        Date currentTime = new Date(System.currentTimeMillis());

        Invitation actualInvitation = new Invitation(user1AccountID + user2AccountID, user2AccountID, user1AccountID, currentTime);
        this.invitationsRepo.save(actualInvitation);

        // Action
        boolean actualResponse = this.invitationsManager.verifyInvitation(actualInvitation).getStatusCode() == HttpStatus.OK;

        // Assertion Message
        String verifyInvitationTestMessage = "Invalid invitation";
        Assertions.assertTrue(actualResponse, verifyInvitationTestMessage);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID + user2AccountID);
    }
    @Test
    public void verifyInvitationInvalidSenderTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        Date currentTime = new Date(System.currentTimeMillis());

        Invitation actualInvitation = new Invitation(user1AccountID + user2AccountID, null, user2AccountID, currentTime);
        this.invitationsRepo.save(actualInvitation);

        // Action
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.invitationsManager.verifyInvitation(actualInvitation).getBody())).get("Message");
        String expectedResponse = "Invalid senderID";
        // Assertion Message
        String verifyInvitationTestMessage = "Invalid invitation";
        Assertions.assertEquals(actualResponse, expectedResponse, verifyInvitationTestMessage);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID + user2AccountID);

    }
    @Test
    public void verifyInvitationInvalidReceiverIDTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        Date currentTime = new Date(System.currentTimeMillis());

        Invitation actualInvitation = new Invitation(user1AccountID + user2AccountID, user1AccountID, null, currentTime);
        this.invitationsRepo.save(actualInvitation);

        // Action
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.invitationsManager.verifyInvitation(actualInvitation).getBody())).get("Message");
        String expectedResponse = "Invalid receiverID";
        // Assertion Message
        String verifyInvitationTestMessage = "Invalid invitation";
        Assertions.assertEquals(actualResponse, expectedResponse, verifyInvitationTestMessage);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID + user2AccountID);

    }
    @Test
    public void verifyInvitationInvalidTimeStampTest() {
        // Values
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();

        Invitation actualInvitation = new Invitation(user1AccountID + user2AccountID, user1AccountID, user2AccountID, null);
        this.invitationsRepo.save(actualInvitation);

        // Action
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.invitationsManager.verifyInvitation(actualInvitation).getBody())).get("Message");
        String expectedResponse = "Invalid TimeStamp";

        // Assertion Message
        String verifyInvitationTestMessage = "Valid TimeStamp";
        Assertions.assertEquals(actualResponse, expectedResponse, verifyInvitationTestMessage);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID + user1AccountID);

    }
    @Test
    public void verifyInvitationSameSenderAndReceiverTest() {
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        Date currentTime = new Date(System.currentTimeMillis());

        // Action
        Invitation actualInvitation = new Invitation(user1AccountID + user1AccountID, user1AccountID, user1AccountID, currentTime);
        this.invitationsRepo.save(actualInvitation);

        // Action
        String actualResponse = (String) ((JSONObject) Objects.requireNonNull(this.invitationsManager.verifyInvitation(actualInvitation).getBody())).get("Message");
        String expectedResponse = "Sender and Receiver are the same!";

        // Assertion Message
        String verifyInvitationTestMessage = "Sender and Receiver are not same";
        Assertions.assertEquals(actualResponse, expectedResponse, verifyInvitationTestMessage);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID + user1AccountID);
    }
    @Test
    public void saveInvitationTest() {
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        Date currentTime = new Date(System.currentTimeMillis());

        Invitation actualInvitation = new Invitation(user1AccountID + user2AccountID, user1AccountID, user2AccountID, currentTime);

        // Action
        this.invitationsManager.saveInvitation(actualInvitation);
        boolean actualValue = this.invitationsRepo.existsById(user1AccountID + user2AccountID);

        // Assertion Message
        String saveInvitationMessage = "invitation does not exist";

        // Assertion Statement
        Assertions.assertTrue(actualValue, saveInvitationMessage);

        // Custom Cleanup
        this.invitationsRepo.deleteById(user1AccountID + user2AccountID);
    }
    @Test
    public void deleteInvitationTest() {
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        Date currentTime = new Date(System.currentTimeMillis());

        Invitation actualInvitation = new Invitation(user1AccountID + user2AccountID, user1AccountID, user2AccountID, currentTime);

        // Action
        this.invitationsManager.saveInvitation(actualInvitation);
        String actualResponse = (String) this.invitationsManager.deleteInvitation(user2AccountID, user1AccountID).getBody();
        String expectedResponse = "Invitation successfully deleted!";

        // Assertion Message
        String deleteInvitationMessage = "Invitation unsuccessfully deleted";

        // Assertion Statement
        Assertions.assertEquals(actualResponse, expectedResponse, deleteInvitationMessage);
        Assertions.assertNull(this.invitationsRepo.findBySenderIDAndReceiverID(user1AccountID, user2AccountID), deleteInvitationMessage);

    }
    @Test
    public void getAllByReceiverIDTest() {
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();
        Date currentTime1 = new Date(System.currentTimeMillis());
        Date currentTime2 = new Date(System.currentTimeMillis());

        Invitation invitation1 = new Invitation(user2AccountID + user1AccountID, user2AccountID, user1AccountID, currentTime1);
        Invitation invitation2 = new Invitation(user3AccountID + user1AccountID, user3AccountID, user1AccountID, currentTime2);

        this.invitationsManager.saveInvitation(invitation1);
        this.invitationsManager.saveInvitation(invitation2);

        // Action
        List<Invitation> senderList = this.invitationsManager.getAllByReceiverID(user1AccountID);
        ArrayList<String> actualList = new ArrayList<>();

        for(Invitation invitation: senderList) {
            actualList.add(invitation.getSenderID());
        }

        // Assertion Message
        String findAllByReceiverIDMessage = "invalid invitation query!";

        // Assertion Statements

        Assertions.assertEquals(2, actualList.size(), findAllByReceiverIDMessage);
        Assertions.assertTrue(actualList.contains(user2AccountID), findAllByReceiverIDMessage);
        Assertions.assertTrue(actualList.contains(user3AccountID), findAllByReceiverIDMessage);

        // Cleanup
        this.invitationsRepo.deleteById(user2AccountID + user1AccountID);
        this.invitationsRepo.deleteById(user3AccountID + user1AccountID);
    }
    @Test
    public void getAllBySenderIDTest() {
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();
        Date currentTime1 = new Date(System.currentTimeMillis());
        Date currentTime2 = new Date(System.currentTimeMillis());

        Invitation invitation1 = new Invitation(user1AccountID + user2AccountID, user1AccountID, user2AccountID, currentTime1);
        Invitation invitation2 = new Invitation(user1AccountID + user3AccountID, user1AccountID, user3AccountID, currentTime2);

        this.invitationsManager.saveInvitation(invitation1);
        this.invitationsManager.saveInvitation(invitation2);

        // Action
        List<Invitation> receiverList = this.invitationsManager.getAllBySenderID(user1AccountID);
        ArrayList<String> actualList = new ArrayList<>();

        for(Invitation invitation: receiverList) {
            actualList.add(invitation.getReceiverID());
        }

        // Assertion Message
        String findAllByReceiverIDMessage = "invalid invitation query!";

        // Assertion Statements

        Assertions.assertEquals(2, actualList.size(), findAllByReceiverIDMessage);
        Assertions.assertTrue(actualList.contains(user2AccountID), findAllByReceiverIDMessage);
        Assertions.assertTrue(actualList.contains(user3AccountID), findAllByReceiverIDMessage);

        // Cleanup
        this.invitationsRepo.deleteById(user1AccountID + user2AccountID);
        this.invitationsRepo.deleteById(user1AccountID + user3AccountID);
    }
    @Test
    public void deleteAllInvitationsRelatedToUserTest() {
        String user1AccountID = Objects.requireNonNull(this.accountManager.verifySession(user1SessionID)).getID();
        String user2AccountID = Objects.requireNonNull(this.accountManager.verifySession(user2SessionID)).getID();
        String user3AccountID = Objects.requireNonNull(this.accountManager.verifySession(user3SessionID)).getID();
        Date currentTime1 = new Date(System.currentTimeMillis());
        Date currentTime2 = new Date(System.currentTimeMillis());

        Invitation invitation1 = new Invitation(user1AccountID + user2AccountID, user1AccountID, user2AccountID, currentTime1);
        Invitation invitation2 = new Invitation(user3AccountID + user1AccountID, user3AccountID, user1AccountID, currentTime2);

        this.invitationsManager.saveInvitation(invitation1);
        this.invitationsManager.saveInvitation(invitation2);

        // First half of Action
        boolean check1 = this.invitationsRepo.findBySenderIDAndReceiverID(user1AccountID, user2AccountID) != null;
        boolean check2 = this.invitationsRepo.findBySenderIDAndReceiverID(user3AccountID, user1AccountID) != null;

        // First half of Assertion Statement
        Assertions.assertTrue(check1);
        Assertions.assertTrue(check2);

        // Section half of Action
        List<Invitation> invitations = new ArrayList<>();
        invitations.add(invitation1);
        invitations.add(invitation2);
        this.invitationsManager.deleteAllInvitationsRelatedToUser(invitations);

        boolean check3 = this.invitationsRepo.findBySenderIDAndReceiverID(user1AccountID, user2AccountID) == null;
        boolean check4 = this.invitationsRepo.findBySenderIDAndReceiverID(user3AccountID, user1AccountID) == null;

        // Second half of Assertion Statement
        Assertions.assertTrue(check3);
        Assertions.assertTrue(check4);
    }
}
