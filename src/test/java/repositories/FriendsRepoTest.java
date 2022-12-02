package repositories;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.SessionID;
import com.backend.repositories.FriendsRepo;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.facades.FriendSystemFacade;
import com.backend.usecases.managers.FriendsManager;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendsRepoTest {
    private final FriendsRepo friendsRepo;
    private final AccountSystemFacade accountSystemFacade;
    private final FriendsManager friendsManager;
    private SessionID sessionID;

    private final String username = "username";
    private final String password = "abc123!";

    @Autowired
    public FriendsRepoTest(FriendsRepo friendsRepo, AccountSystemFacade accountSystemFacade, FriendsManager friendsManager, FriendsRepo friendsRepo1, FriendSystemFacade friendSystemFacade1, FriendsManager friendsManager1) {
        this.friendsRepo = friendsRepo;
        this.accountSystemFacade = accountSystemFacade;
        this.friendsManager = friendsManager;
    }

    @BeforeEach
    public void setup() {
        sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(username, password).getBody())).get("sessionID"));
    }

    @AfterEach
    public void tearDown() {
        this.accountSystemFacade.logoutAccount(sessionID);
    }

    @Test
    public void testFindAllContainingUserID() {

    }


}
