package usecases.facades;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.ProtectedAccount;
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
public class AccountSystemFacadeTest {
    private final AccountsRepo accountsRepo;
    private final AccountManager accountManager;
    private final AccountSystemFacade accountSystemFacade;
    private SessionID sessionID;
    private final String username = "username";
    private final String password = "abc123!";

    @Autowired
    public AccountSystemFacadeTest(AccountsRepo accountsRepo, AccountSystemFacade accountSystemFacade, AccountManager accountManager) {
        this.accountsRepo = accountsRepo;
        this.accountManager = accountManager;
        this.accountSystemFacade = accountSystemFacade;
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
    public void getAccountInfoTest() {
        // Values
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(new ProtectedAccount(username, null), HttpStatus.OK);

        // Action
        ResponseEntity<Object> actualResponse = this.accountSystemFacade.getAccountInfo(sessionID);

        // Assertion Message
        String accountInfoMessage = "The given sessionID didn't yield the corresponding account information";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode(), accountInfoMessage);
        Assertions.assertEquals(((ProtectedAccount) Objects.requireNonNull(expectedResponse.getBody())).getUsername(), ((ProtectedAccount) Objects.requireNonNull(actualResponse.getBody())).getUsername(), accountInfoMessage);
    }

    @Test
    public void getAccountInfoInvalidTest() {
        // Values
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(new ProtectedAccount(username, null), HttpStatus.BAD_REQUEST);

        // Action
        ResponseEntity<Object> actualResponse = this.accountSystemFacade.getAccountInfo(new SessionID("invalidID"));

        // Assertion Message
        String accountInfoMessage = "The given sessionID didn't yield the corresponding account information";

        // Assertion Statement
        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode(), accountInfoMessage);
    }

    @Test
    public void registerTest() {
        // Setup
        this.accountSystemFacade.deleteAccount(sessionID);

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.registerAccount(username, password);
        sessionID = new SessionID(((JSONObject) Objects.requireNonNull(responseEntity.getBody())).get("sessionID").toString());

        // Assertion Message
        String registerMessage = "Could not register an account with valid credentials";

        // Assertion Statement
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), registerMessage);
        Assertions.assertTrue(this.accountsRepo.existsById(Objects.requireNonNull(this.accountManager.verifySession(sessionID)).getID()), registerMessage);
    }

    @Test
    public void registerAlreadyExistsTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.registerAccount(username, password);

        // Assertion Message
        String registerMessage = "Account was unexpectedly 'created' when given existing credentials";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, registerMessage);
    }

    @Test
    public void registerInvalidInfoTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.registerAccount(username, "invalidPassword");

        // Assertion Message
        String registerMessage = "Account was unexpectedly 'created' when given invalid credentials";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, registerMessage);
    }

    @Test
    public void loginTest() {
        // Setup
        this.accountSystemFacade.logoutAccount(sessionID);

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.loginAccount(username, password);
        sessionID = new SessionID(((JSONObject) Objects.requireNonNull(responseEntity.getBody())).get("sessionID").toString());

        // Assertion Message
        String loginMessage = "Could not login an account with valid credentials";

        // Assertion Statement
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), loginMessage);
        Assertions.assertTrue(this.accountsRepo.existsById(Objects.requireNonNull(this.accountManager.verifySession(sessionID)).getID()), loginMessage);
    }

    @Test
    public void loginAlreadyLoggedInTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.loginAccount(username, password);

        // Assertion Message
        String loginMessage = "Account was unexpectedly 'logged in' when given credentials of an already logged in account";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.UNAUTHORIZED, loginMessage);
    }

    @Test
    public void loginInvalidInfoTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.loginAccount(username, "invalidPassword");

        // Assertion Message
        String loginMessage = "Account was unexpectedly 'logged in' when given invalid credentials";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, loginMessage);
    }

    @Test
    public void loginAccountDoesNotExistTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.loginAccount("notUsername", password);

        // Assertion Message
        String loginMessage = "Account was unexpectedly 'logged in' when given credentials of account that doesn't exist";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND, loginMessage);
    }

    @Test
    public void logoutTest() {
        // Setup (not Required)

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.logoutAccount(sessionID);

        // Assertion Message
        String logoutMessage = "Could not logout an account with valid credentials";

        // Assertion Statement
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), logoutMessage);
        Assertions.assertNull(this.accountsRepo.findBySessionID(sessionID.getID()), logoutMessage);
    }

    @Test
    public void logoutAlreadyLoggedOutTest() {
        // Setup
        this.accountSystemFacade.logoutAccount(sessionID);

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.logoutAccount(sessionID);

        // Assertion Message
        String logoutMessage = "Account was unexpectedly 'logged out' when given credentials of an already logged in account";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, logoutMessage);
    }

    @Test
    public void logoutInvalidInfoTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.logoutAccount(new SessionID("invalidID"));

        // Assertion Message
        String logoutMessage = "Account was unexpectedly 'logged out' when given invalid credentials";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, logoutMessage);
    }

    @Test
    public void logoutAccountDoesNotExistTest() {
        // Setup (Not Required)

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.logoutAccount(new SessionID("diajmdlanmdln80"));

        // Assertion Message
        String logoutMessage = "Account was unexpectedly 'logged in' when given credentials of account that doesn't exist";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, logoutMessage);
    }

    @Test
    public void deleteTest() {
        // Setup (not Required)

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.deleteAccount(sessionID);

        // Assertion Message
        String deleteMessage = "Could not delete an account with valid credentials";

        // Assertion Statement
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), deleteMessage);
        Assertions.assertNull(this.accountsRepo.findBySessionID(sessionID.getID()), deleteMessage);
    }

    @Test
    public void deleteUnauthorizedTest() {
        // Setup
        this.accountSystemFacade.logoutAccount(sessionID);

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.deleteAccount(sessionID);

        // Assertion Message
        String deleteMessage = "Account was unexpectedly 'deleted' when given unauthorized credentials";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, deleteMessage);
    }

    @Test
    public void deleteInvalidInfoTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.logoutAccount(new SessionID("invalidID"));

        // Assertion Message
        String deleteMessage = "Account was unexpectedly 'deleted' when given invalid credentials";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, deleteMessage);
    }

    @Test
    public void deleteAccountDoesNotExistTest() {
        // Setup (Not Required)

        // Action
        ResponseEntity<Object> responseEntity = this.accountSystemFacade.logoutAccount(new SessionID("diajmdlanmdln80"));

        // Assertion Message
        String deleteMessage = "Account was unexpectedly 'delete' when given credentials of account that doesn't exist";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, deleteMessage);
    }
}
