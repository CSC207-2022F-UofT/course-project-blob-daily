package repositories;

import com.backend.QuestPetsApplication;
import com.backend.entities.Friend;
import com.backend.entities.IDs.SessionID;
import com.backend.repositories.FriendsRepo;
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
import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendsRepoTest {
    private final FriendsRepo friendsRepo;
    private final AccountSystemFacade accountSystemFacade;

    private final AccountManager accountManager;
    private SessionID userSessionID;
    private SessionID friend1SessionID;
    private SessionID friend2SessionID;

    @Autowired
    public FriendsRepoTest(FriendsRepo friendsRepo, AccountSystemFacade accountSystemFacade, AccountManager accountManager) {
        this.friendsRepo = friendsRepo;
        this.accountManager = accountManager;
        this.accountSystemFacade = accountSystemFacade;
    }

    @BeforeEach
    public void setup() {
        String username = "username";
        String password = "abc123!";
        userSessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(username, password).getBody())).get("sessionID"));
        String friend1Username = "potter";
        friend1SessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(friend1Username, password).getBody())).get("sessionID"));
        String friend2Username = "peter";
        friend2SessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(friend2Username, password).getBody())).get("sessionID"));
    }

    @AfterEach
    public void tearDown() {
        this.accountSystemFacade.logoutAccount(userSessionID);
        this.accountSystemFacade.logoutAccount(friend1SessionID);
        this.accountSystemFacade.logoutAccount(friend2SessionID);
        this.friendsRepo.deleteById(userSessionID.getID());
        this.friendsRepo.deleteById(friend1SessionID.getID());
        this.friendsRepo.deleteById(friend2SessionID.getID());
    }

    @Test
    public void testFindAllContainingUserID() {
        // Values
        String userAccountID = Objects.requireNonNull(this.accountManager.verifySession(userSessionID)).getID();
        String friend1AccountID = Objects.requireNonNull(this.accountManager.verifySession(friend1SessionID)).getID();
        String friend2AccountID = Objects.requireNonNull(this.accountManager.verifySession(friend2SessionID)).getID();

        ArrayList<String> userList = new ArrayList<>();
        userList.add(friend1AccountID);
        userList.add(friend2AccountID);
        Friend user = new Friend(userAccountID, userList);

        this.friendsRepo.save(user);

        ArrayList<String> friend1List = new ArrayList<>();
        friend1List.add(userAccountID);
        friend1List.add(friend2AccountID);
        Friend friend1 = new Friend(friend1AccountID, friend1List);
        this.friendsRepo.save(friend1);

        ArrayList<String> friend2List = new ArrayList<>();
        friend2List.add(userAccountID);
        friend2List.add(friend1AccountID);
        Friend friend2 = new Friend(friend2AccountID, friend2List);
        this.friendsRepo.save(friend2);


        // Action
        List<Friend> allCorrelatedList = friendsRepo.findAllContainingUserID(userAccountID);
        ArrayList<String> actualList = new ArrayList<>();

        // Assertion Message
        String findAllContainingUserIDMessage = "user is not in retrieved friends list";

        for(Friend friend : allCorrelatedList) {
            actualList.add(friend.getAccountID());
        }

        // Assertion Statement
        Assertions.assertTrue(actualList.contains(friend1AccountID) && actualList.contains(friend2AccountID), findAllContainingUserIDMessage);
    }


}
