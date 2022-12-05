package usecases.facades;

import com.backend.QuestPetsApplication;
import com.backend.entities.FeedItem;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Pet;
import com.backend.entities.TaskCompletionRecord;
import com.backend.entities.users.ProtectedAccount;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.facades.FeedSystemFacade;
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
public class FeedSystemFacadeTest {
    private final AccountSystemFacade accountSystemFacade;
    private final FeedSystemFacade feedSystemFacade;
    private SessionID sessionID;

    @Autowired
    public FeedSystemFacadeTest(AccountSystemFacade accountSystemFacade, FeedSystemFacade feedSystemFacade) {
        this.accountSystemFacade = accountSystemFacade;
        this.feedSystemFacade = feedSystemFacade;
    }

    @BeforeEach
    public void setup() {
        String username = "username";
        String password = "abc123!";
        ResponseEntity<Object> register = this.accountSystemFacade.registerAccount(username, password);

        if (!(register.getStatusCode() == HttpStatus.OK)){
            sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(username, password).getBody())).get("sessionID"));
        } else  {
            sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(register.getBody())).get("sessionID"));
        }

    }

    @AfterEach
    public void tearDown() {
        if (sessionID != null) {
            this.accountSystemFacade.logoutAccount(this.sessionID);
        }
    }

    @Test
    public void getFeedTest() {
        // Values
        ProtectedAccount expectedAccount = new ProtectedAccount("usernameFriend");
        TaskCompletionRecord expectedTaskCompletionRecord = new TaskCompletionRecord(new AccountID(null), null, null, null);
        Pet expectedPet = new Pet(null, null, null, null, null);
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(new ArrayList<>(List.of(new FeedItem(expectedAccount, expectedTaskCompletionRecord, expectedPet))), HttpStatus.OK);

        // Action
        ResponseEntity<Object> actualResponse = this.feedSystemFacade.getFeed(sessionID.getID());

        // Assertion Message
        String feedMessage = "The given sessionID didn't yield the corresponding feed information";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode(), feedMessage);
    }

    @Test
    public void getFeedInvalidSessionTest() {
        // Values (Not Required)

        // Action
        ResponseEntity<Object> actualResponse = this.feedSystemFacade.getFeed("InvalidSession");

        // Assertion Message
        String feedMessage = "The given sessionID unexpectedly yielded the corresponding feed information";

        // Assertion Statement
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode(), feedMessage);
    }
}
