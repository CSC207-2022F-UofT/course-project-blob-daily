package controller;

import com.backend.QuestPetsApplication;
import com.backend.controller.AccountController;
import com.backend.entities.IDs.SessionID;
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

    @Autowired
    AccountController accountController;

    private SessionID sessionID;
    private final String username = "username";
    private final String password = "abc123!";

    @BeforeEach
    public void setup() {
        sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(AccountManager.loginAccount(username, password).getBody())).get("sessionID"));
    }

    @AfterEach
    public void tearDown() {
        AccountManager.logoutAccount(sessionID);
    }

    @Test
    public void loginAccountTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.OK;

        AccountManager.logoutAccount(sessionID);

        // Action
        ResponseEntity<Object> actualResponse = accountController.loginAccount(username, password);
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

        AccountManager.logoutAccount(sessionID);

        // Action
        ResponseEntity<Object> actualResponse = accountController.loginAccount(username, "lad123!");

        // Assertion Message
        String loginAccountMessage = "Unexpectedly able to login to a existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), loginAccountMessage);
    }

    @Test
    public void loginAccountInvalidSessionIDTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        AccountManager.logoutAccount(sessionID);

        // Action
        ResponseEntity<Object> actualResponse = accountController.loginAccount(username, "lad123");

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
        ResponseEntity<Object> actualResponse = accountController.loginAccount(username, password);

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
        ResponseEntity<Object> actualResponse = accountController.logoutAccount(sessionID.getID());

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
        ResponseEntity<Object> actualResponse = accountController.logoutAccount("");

        // Assertion Message
        String logoutAccountMessage = "Unexpectedly able to logout to a existent account given invalid credentials";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), logoutAccountMessage);
        Assertions.assertNotNull(Objects.requireNonNull(actualResponse.getBody()), logoutAccountMessage);
    }
}
