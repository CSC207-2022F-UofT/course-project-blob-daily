package repositories;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.Account;
import com.backend.repositories.AccountsRepo;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.managers.AccountManager;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountsRepoTest {
    private final AccountsRepo accountsRepo;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;
    private SessionID sessionID;
    private final String username = "username";
    private final String password = "abc123!";

    @Autowired
    public AccountsRepoTest(AccountsRepo accountsRepo, AccountSystemFacade accountSystemFacade, AccountManager accountManager) {
        this.accountsRepo = accountsRepo;
        this.accountSystemFacade = accountSystemFacade;
        this.accountManager = accountManager;
    }

    @BeforeEach
    public void setup() {
        ResponseEntity<Object> register = this.accountSystemFacade.registerAccount(this.username, this.password);

        if (!(register.getStatusCode() == HttpStatus.OK)){
            sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(this.username, this.password).getBody())).get("sessionID"));
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
    public void findByAccountIDTest() {
        // Values
        String expectedAccountID = Objects.requireNonNull(this.accountManager.verifySession(sessionID)).getID();
        String expectedSessionID = sessionID.getID();

        // Action
        Account account = accountsRepo.findByAccountID(expectedAccountID);

        // Assertion Message
        String findByAccountIDMessage = "Invalid account provided for given accountID";

        // Assertion Statements
        Assertions.assertEquals(username, account.getUsername(), findByAccountIDMessage);
        Assertions.assertEquals(this.accountManager.hash(password), account.getPassword(), findByAccountIDMessage);
        Assertions.assertEquals(expectedSessionID, account.getSessionID(), findByAccountIDMessage);
        Assertions.assertEquals(expectedAccountID, account.getAccountID(), findByAccountIDMessage);
    }

    @Test
    public void findBySessionIDTest() {
        // Values
        String expectedAccountID = Objects.requireNonNull(this.accountManager.verifySession(sessionID)).getID();
        String expectedSessionID = sessionID.getID();

        // Action
        Account account = accountsRepo.findBySessionID(expectedSessionID);

        // Assertion Message
        String findBySessionIDMessage = "Invalid account provided for given sessionID";

        // Assertion Statements
        Assertions.assertEquals(username, account.getUsername(), findBySessionIDMessage);
        Assertions.assertEquals(this.accountManager.hash(password), account.getPassword(), findBySessionIDMessage);
        Assertions.assertEquals(expectedSessionID, account.getSessionID(), findBySessionIDMessage);
        Assertions.assertEquals(expectedAccountID, account.getAccountID(), findBySessionIDMessage);
    }

    @Test
    public void findByCredentialsTest() {
        // Values
        String expectedAccountID = Objects.requireNonNull(this.accountManager.verifySession(sessionID)).getID();
        String expectedSessionID = sessionID.getID();

        // Action
        Account account = accountsRepo.findByCredentials(username, this.accountManager.hash(password));

        // Assertion Message
        String findByCredentialsMessage = "Invalid account provided for given credentials";

        // Assertion Statements
        Assertions.assertEquals(username, account.getUsername(), findByCredentialsMessage);
        Assertions.assertEquals(this.accountManager.hash(password), account.getPassword(), findByCredentialsMessage);
        Assertions.assertEquals(expectedSessionID, account.getSessionID(), findByCredentialsMessage);
        Assertions.assertEquals(expectedAccountID, account.getAccountID(), findByCredentialsMessage);
    }

    @Test
    public void findByUsernameTest() {
        // Values
        String expectedAccountID = Objects.requireNonNull(this.accountManager.verifySession(sessionID)).getID();
        String expectedSessionID = sessionID.getID();

        // Action
        Account account = accountsRepo.findByUsername(username);

        // Assertion Message
        String findByUsernameMessage = "Invalid account provided for given username";

        // Assertion Statements
        Assertions.assertEquals(username, account.getUsername(), findByUsernameMessage);
        Assertions.assertEquals(this.accountManager.hash(password), account.getPassword(), findByUsernameMessage);
        Assertions.assertEquals(expectedSessionID, account.getSessionID(), findByUsernameMessage);
        Assertions.assertEquals(expectedAccountID, account.getAccountID(), findByUsernameMessage);
    }
}
