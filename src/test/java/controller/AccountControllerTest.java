package controller;

import com.backend.QuestPetsApplication;
import com.backend.controller.AccountController;
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
public class AccountControllerTest {
    private final AccountController accountController;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;
    private final AccountsRepo accountsRepo;
    private SessionID sessionID;
    private final String username = "username";
    private final String password = "abc123!";

    @Autowired
    public AccountControllerTest(AccountController accountController, AccountSystemFacade accountSystemFacade, AccountManager accountManager, AccountsRepo accountsRepo) {
        this.accountController = accountController;
        this.accountSystemFacade = accountSystemFacade;
        this.accountManager = accountManager;
        this.accountsRepo = accountsRepo;
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
    public void loginAccountTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.OK;

        this.accountSystemFacade.logoutAccount(sessionID);

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.loginAccount(username, password);
        sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(actualResponse.getBody())).get("sessionID"));

        // Assertion Message
        String loginAccountMessage = "Unexpectedly unable to login to a existent account given valid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), loginAccountMessage);
        Assertions.assertNotNull(((JSONObject) Objects.requireNonNull(actualResponse.getBody())).get("sessionID"), loginAccountMessage);
    }

    @Test
    public void loginAccountInvalidCredentialsTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

        this.accountSystemFacade.logoutAccount(sessionID);

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.loginAccount(username, "lad123!");

        // Assertion Message
        String loginAccountMessage = "Unexpectedly able to login to a existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), loginAccountMessage);
    }

    @Test
    public void loginAccountInvalidSessionIDTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        this.accountSystemFacade.logoutAccount(sessionID);

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.loginAccount(username, "lad123");

        // Assertion Message
        String loginAccountMessage = "Unexpectedly able to login to a existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), loginAccountMessage);
    }

    @Test
    public void loginAccountAlreadyLoggedInTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.UNAUTHORIZED;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.loginAccount(username, password);

        // Assertion Message
        String loginAccountMessage = "Unexpectedly able to login to a existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), loginAccountMessage);
    }

    @Test
    public void logoutAccountTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.OK;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.logoutAccount(sessionID.getID());

        // Assertion Message
        String logoutAccountMessage = "Unexpectedly unable to logout to a existent account given valid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), logoutAccountMessage);
        Assertions.assertNotNull(Objects.requireNonNull(actualResponse.getBody()), logoutAccountMessage);
    }

    @Test
    public void logoutAccountInvalidSessionTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.logoutAccount("");

        // Assertion Message
        String logoutAccountMessage = "Unexpectedly able to logout to a existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), logoutAccountMessage);
        Assertions.assertNotNull(Objects.requireNonNull(actualResponse.getBody()), logoutAccountMessage);
    }

    @Test
    public void deleteAccountTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.OK;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.deleteAccount(sessionID.getID());

        // Assertion Message
        String deleteAccountMessage = "Unexpectedly unable to delete to an existent account given valid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), deleteAccountMessage);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, this.accountSystemFacade.getAccountInfo(sessionID).getStatusCode(), deleteAccountMessage);
    }

    @Test
    public void deleteAccountInvalidTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.deleteAccount("invalidID");

        // Assertion Message
        String deleteAccountMessage = "Unexpectedly able to delete to an existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), deleteAccountMessage);
        Assertions.assertEquals(HttpStatus.OK, this.accountSystemFacade.getAccountInfo(sessionID).getStatusCode(), deleteAccountMessage);
    }

    @Test
    public void registerAccountTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.OK;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.registerAccount("newUsername", password);
        SessionID newSessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(actualResponse.getBody())).get("sessionID"));

        // Assertion Message
        String registerAccountMessage = "Unexpectedly unable to register to a existent account given valid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), registerAccountMessage);
        Assertions.assertTrue(this.accountsRepo.existsById(this.accountManager.verifySession(newSessionID).getID()), registerAccountMessage);

        // Special Tear-Down
        this.accountSystemFacade.deleteAccount(newSessionID);
    }

    @Test
    public void registerAccountInvalidInfoTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.registerAccount("invalidUsername829)@)", password);

        // Assertion Message
        String registerAccountMessage = "Unexpectedly able to register to a existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), registerAccountMessage);
    }

    @Test
    public void registerAccountAlreadyExistsTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.registerAccount(username, password);

        // Assertion Message
        String registerAccountMessage = "Unexpectedly able to register to a existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), registerAccountMessage);
    }

    @Test
    public void getAccountTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.OK;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.getAccount(sessionID.getID());

        // Assertion Message
        String getAccountMessage = "Unexpectedly not able to get information for a existent account given valid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), getAccountMessage);
        Assertions.assertEquals(username, ((ProtectedAccount) Objects.requireNonNull(actualResponse.getBody())).getUsername(), getAccountMessage);
    }

    @Test
    public void getAccountInvalidSessionIDTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.getAccount("invalidID");

        // Assertion Message
        String getAccountMessage = "Unexpectedly able to get information for a existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), getAccountMessage);
    }

    @Test
    public void updateAccountUsernameTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.OK;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.updateAccountUsername(sessionID.getID(), "newUsername");

        // Assertion Message
        String getAccountMessage = "Unexpectedly not able to update information for a existent account given valid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), getAccountMessage);
        Assertions.assertNotNull(this.accountManager.validateCredentials("newUsername", this.accountManager.hash(password)), getAccountMessage);

        // Special Tear-down
        this.accountManager.deleteAccount(this.accountManager.validateCredentials("newUsername", this.accountManager.hash(password)).getAccountIDObject());
    }

    @Test
    public void updateAccountUsernameInvalidSessionTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.updateAccountUsername("Invalid", "newUsername");

        // Assertion Message
        String getAccountMessage = "Unexpectedly able to update information for a existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), getAccountMessage);
    }

    @Test
    public void updateAccountUsernameInvalidUsernameTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.updateAccountUsername(sessionID.getID(), "inv");

        // Assertion Message
        String getAccountMessage = "Unexpectedly able to update information for a existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), getAccountMessage);
    }

    @Test
    public void updateAccountPasswordTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.OK;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.updateAccountPassword(sessionID.getID(), "abc124!");

        // Assertion Message
        String getAccountMessage = "Unexpectedly not able to update information for a existent account given valid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), getAccountMessage);
        Assertions.assertNotNull(this.accountManager.validateCredentials(username, this.accountManager.hash("abc124!")), getAccountMessage);

        // Special Tear-down
        this.accountManager.deleteAccount(this.accountManager.validateCredentials(username, this.accountManager.hash("abc124!")).getAccountIDObject());
    }

    @Test
    public void updateAccountPasswordInvalidSessionTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.updateAccountPassword("InvalidID", "abc124!");

        // Assertion Message
        String getAccountMessage = "Unexpectedly able to update information for a existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), getAccountMessage);
    }

    @Test
    public void updateAccountPasswordInvalidPasswordTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        // Action
        ResponseEntity<Object> actualResponse = this.accountController.updateAccountPassword(sessionID.getID(), "InvalidPassword");

        // Assertion Message
        String getAccountMessage = "Unexpectedly able to update information for a existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), getAccountMessage);
    }
}
