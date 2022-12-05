package repositories;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Invitation;
import com.backend.repositories.InvitationsRepo;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.managers.AccountManager;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvitationsRepoTest {
    private final InvitationsRepo invitationsRepo;
    private final AccountManager accountManager;
    private final AccountSystemFacade accountSystemFacade;

    private SessionID userSessionID;
    private SessionID friendSessionID1;
    private SessionID friendSessionID2;

    @Autowired
    public InvitationsRepoTest(InvitationsRepo invitationsRepo, AccountManager accountManager, AccountSystemFacade accountSystemFacade) {
        this.invitationsRepo = invitationsRepo;
        this.accountManager = accountManager;
        this.accountSystemFacade = accountSystemFacade;
    }

    @BeforeEach
    public void setup() {
        String username = "dummy1";
        String friendUsername1 = "dummy2";
        String friendUsername2 = "dummy3";
        String password = "abc123!";
        userSessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(username, password).getBody())).get("sessionID"));
        friendSessionID1 = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(friendUsername1, password).getBody())).get("sessionID"));
        friendSessionID2 = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(friendUsername2, password).getBody())).get("sessionID"));
    }

    @AfterEach
    public void tearDown() {
        this.accountSystemFacade.logoutAccount(userSessionID);
        this.accountSystemFacade.logoutAccount(friendSessionID1);
        this.accountSystemFacade.logoutAccount(friendSessionID2);
    }

    @Test
    public void testFindBySenderIDAndReceiverID() {
        // Values
        String senderID = this.accountManager.verifySession(userSessionID).getID();
        String receiverID = this.accountManager.verifySession(friendSessionID1).getID();
        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(senderID+receiverID, senderID, receiverID, timestamp));

        // Action
        Invitation actualInvitation = invitationsRepo.findBySenderIDAndReceiverID(senderID, receiverID);

        // Assertion Message
        String findBySenderIDAndReceiverIDMessage = "invalid invitation query!";

        // Assertion Statements
        Assertions.assertEquals(actualInvitation.getSenderID(), senderID, findBySenderIDAndReceiverIDMessage);
        Assertions.assertEquals(actualInvitation.getReceiverID(), receiverID, findBySenderIDAndReceiverIDMessage);
        Assertions.assertEquals(actualInvitation.getTimestamp(), timestamp, findBySenderIDAndReceiverIDMessage);

        // Cleanup
        this.invitationsRepo.deleteById(senderID + receiverID);
    }

    @Test
    public void testFindAllByReceiverID() {
        // Values
        String receiverID = this.accountManager.verifySession(userSessionID).getID();
        String senderID1 = this.accountManager.verifySession(friendSessionID1).getID();
        String senderID2 = this.accountManager.verifySession(friendSessionID2).getID();
        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(senderID1+receiverID, senderID1, receiverID, timestamp));
        this.invitationsRepo.save(new Invitation(senderID2+receiverID, senderID2, receiverID, timestamp));

        // Action
        List<Invitation> senderList = this.invitationsRepo.findAllByReceiverID(receiverID);
        ArrayList<String> actualList = new ArrayList<>();

        for(Invitation invitation: senderList) {
            actualList.add(invitation.getSenderID());
        }

        // Assertion Message
        String findAllByReceiverIDMessage = "invalid invitation query!";

        // Assertion Statements

        Assertions.assertEquals(2, actualList.size(), findAllByReceiverIDMessage);
        Assertions.assertTrue(actualList.contains(senderID1), findAllByReceiverIDMessage);
        Assertions.assertTrue(actualList.contains(senderID2), findAllByReceiverIDMessage);

        // Cleanup
        this.invitationsRepo.deleteById(senderID1 + receiverID);
        this.invitationsRepo.deleteById(senderID2 + receiverID);
    }

    @Test
    public void testFindAllBySenderID() {
        // Values
        String senderID = this.accountManager.verifySession(userSessionID).getID();
        String receiverID1 = this.accountManager.verifySession(friendSessionID1).getID();
        String receiverID2 = this.accountManager.verifySession(friendSessionID2).getID();
        Date timestamp = new Date(System.currentTimeMillis());

        this.invitationsRepo.save(new Invitation(senderID+receiverID1, senderID, receiverID1, timestamp));
        this.invitationsRepo.save(new Invitation(senderID+receiverID2, senderID, receiverID2, timestamp));

        // Action
        List<Invitation> receiverList = this.invitationsRepo.findAllBySenderID(senderID);
        ArrayList<String> actualList = new ArrayList<>();

        for(Invitation invitation: receiverList) {
            actualList.add(invitation.getReceiverID());
        }

        // Assertion Message
        String findAllByReceiverIDMessage = "invalid invitation query!";

        // Assertion Statements
        Assertions.assertEquals(2, actualList.size(), findAllByReceiverIDMessage);
        Assertions.assertTrue(actualList.contains(receiverID1), findAllByReceiverIDMessage);
        Assertions.assertTrue(actualList.contains(receiverID2), findAllByReceiverIDMessage);

        // Cleanup
        this.invitationsRepo.deleteById(senderID + receiverID1);
        this.invitationsRepo.deleteById(senderID + receiverID2);
    }
}
