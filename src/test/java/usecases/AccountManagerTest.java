package usecases;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.Account;
import com.backend.entities.users.ProtectedAccount;
import com.backend.usecases.AccountManager;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountManagerTest {

    private SessionID sessionID;
    private final String username = "username";
    private final String password = "abc123!";

    @BeforeEach
    public void setup() {
        ResponseEntity<Object> register = AccountManager.registerAccount(this.username, this.password);

        if (!(register.getStatusCode() == HttpStatus.OK)){
            sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(AccountManager.loginAccount(this.username, this.password).getBody())).get("sessionID"));
        } else  {
            sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(register.getBody())).get("sessionID"));
        }

    }

    @AfterEach
    public void tearDown() {
        if (sessionID != null) {
            AccountManager.logoutAccount(this.sessionID);
        }
    }

    @Test
    public void verifySessionTest() {
        // Action
        String actualAccountID = Objects.requireNonNull(AccountManager.verifySession(this.sessionID)).getID();

        // Assertion Message
        String verifySessionMessage = String.format("The given valid ID returned an unexpected accountID '%s'",
                actualAccountID);

        // Assertion Statement
        Assertions.assertTrue(AccountManager.accountsRepo.existsById(actualAccountID), verifySessionMessage);
    }

    @Test
    public void verifySessionInvalidTest() {
        // Values
        String invalidSessionID = "deal28check";

        // Action
        Object actual = AccountManager.verifySession(new SessionID(invalidSessionID));

        // Assertion Message
        String verifySessionInvalidMessage = String.format("The given invalid ID '%s' returned an unexpected result",
                invalidSessionID);

        // Assertion Statement
        Assertions.assertNull(actual, verifySessionInvalidMessage);
    }

    @Test
    public void verifyAccountInfoProtectedAccountTest() {
        // Values
        Date timestamp = new Date(System.currentTimeMillis());

        ProtectedAccount protectedAccount = new ProtectedAccount(username, timestamp);

        // Action
        boolean resultProtectedAccount = AccountManager.verifyAccountInfo(protectedAccount);

        // Assertion Message
        String verifyAccountInfoMessage = "The given valid account returned invalid (false) unexpectedly";

        // Assertion Statement
        Assertions.assertTrue(resultProtectedAccount, verifyAccountInfoMessage);
    }

    @Test
    public void verifyAccountInfoAccountByAccountIDTest() {
        // Values
        AccountID accountID = new AccountID("9XlRk9W&Bc3ulLe2TlKl");

        Date timestamp = new Date(System.currentTimeMillis());

        Account accountByAccountID = new Account(accountID, username, password, timestamp);
        accountByAccountID.getSessionIDObject().generateID();

        // Action
        boolean resultAccountByAccountID = AccountManager.verifyAccountInfo(accountByAccountID);

        // Assertion Message
        String verifyAccountInfoMessage = "The given valid account returned invalid (false) unexpectedly";

        // Assertion Statement
        Assertions.assertTrue(resultAccountByAccountID, verifyAccountInfoMessage);
    }

    @Test
    public void verifyAccountInfoAccountBySessionIDTest() {
        // Values
        SessionID sessionID = new SessionID("0lAi3uAw1MPe1SDk");

        Account accountBySessionID = new Account(sessionID, username, password);
        accountBySessionID.getAccountIDObject().generateID();
        accountBySessionID.getSessionIDObject().generateID();

        // Action
        boolean resultAccountBySessionID = AccountManager.verifyAccountInfo(accountBySessionID);

        // Assertion Message
        String verifyAccountInfoMessage = "The given valid account returned invalid (false) unexpectedly";

        // Assertion Statement
        Assertions.assertTrue(resultAccountBySessionID, verifyAccountInfoMessage);
    }

    @Test
    public void verifyInvalidAccountInfoProtectedAccountTest() {
        // Values

        ProtectedAccount protectedAccount = new ProtectedAccount(username, null);

        // Action
        boolean resultProtectedAccount = AccountManager.verifyAccountInfo(protectedAccount);

        // Assertion Message
        String verifyAccountInfoMessage = "The given invalid account returned valid (true) unexpectedly";

        // Assertion Statement
        Assertions.assertFalse(resultProtectedAccount, verifyAccountInfoMessage);
    }

    @Test
    public void verifyInvalidAccountInfoAccountByAccountIDTest() {
        // Values
        AccountID accountID = new AccountID("9XlRk9W&");

        Date timestamp = new Date(System.currentTimeMillis());

        Account accountByAccountID = new Account(accountID, username, password, timestamp);
        accountByAccountID.getSessionIDObject().generateID();

        // Action
        boolean resultAccountByAccountID = AccountManager.verifyAccountInfo(accountByAccountID);

        // Assertion Message
        String verifyAccountInfoMessage = "The given invalid account returned valid (true) unexpectedly";

        // Assertion Statement
        Assertions.assertFalse(resultAccountByAccountID, verifyAccountInfoMessage);
    }

    @Test
    public void verifyInvalidAccountInfoAccountBySessionIDTest() {
        // Values
        SessionID sessionID = new SessionID("0lAi3uAw1M");

        Account accountBySessionID = new Account(sessionID, username, password);
        accountBySessionID.getAccountIDObject().generateID();

        // Action
        boolean resultAccountBySessionID = AccountManager.verifyAccountInfo(accountBySessionID);

        // Assertion Message
        String verifyAccountInfoMessage = "The given invalid account returned valid (true) unexpectedly";

        // Assertion Statement
        Assertions.assertFalse(resultAccountBySessionID, verifyAccountInfoMessage);
    }

    @Test
    public void hashStringTest() {
        // Values
        String password = "abc123!";
        String expectedHash = "033b83d92431548e13424903c235a9922af56dd34d53c9b72b37cf158489213e";

        // Action
        String hashedPassword = AccountManager.hash(password);

        // Assertion Message
        String hashMessage = "The given string was not hashed correctly according to the SHA-256 algorithm";

        // Assertion Statement
        Assertions.assertEquals(hashedPassword, expectedHash, hashMessage);
    }

    @Test
    public void getAccountInfoAccountIDTest() {
        // Values
        AccountID accountID = AccountManager.verifySession(sessionID);
        ProtectedAccount expectedAccount = new ProtectedAccount(username, null);

        // Action
        ProtectedAccount actualAccount = (ProtectedAccount) AccountManager.getAccountInfo(accountID).getBody();

        // Assertion Message
        String accountInfoMessage = "The given accountID didn't yield the corresponding account information";

        // Assertion Statement
        assert actualAccount != null;
        Assertions.assertEquals(expectedAccount.getUsername(), actualAccount.getUsername(), accountInfoMessage);
    }

    @Test
    public void getAccountInfoSessionIDTest() {
        // Values
        ProtectedAccount expectedAccount = new ProtectedAccount(username, null);

        // Action
        ProtectedAccount actualAccount = (ProtectedAccount) AccountManager.getAccountInfo(sessionID).getBody();

        // Assertion Message
        String accountInfoMessage = "The given accountID didn't yield the corresponding account information";

        // Assertion Statement
        assert actualAccount != null;
        Assertions.assertEquals(expectedAccount.getUsername(), actualAccount.getUsername(), accountInfoMessage);
    }

    @Test
    public void registerTest() {
        // Setup
        AccountManager.deleteAccount(sessionID);

        // Action
        ResponseEntity<Object> responseEntity = AccountManager.registerAccount(username, password);
        sessionID = new SessionID(((JSONObject) Objects.requireNonNull(responseEntity.getBody())).get("sessionID").toString());

        // Assertion Message
        String registerMessage = "Could not register an account with valid credentials";

        // Assertion Statement
        Assertions.assertTrue(AccountManager.accountsRepo.existsById(Objects.requireNonNull(AccountManager.verifySession(sessionID)).getID()), registerMessage);
    }

    @Test
    public void registerAlreadyExistsTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = AccountManager.registerAccount(username, password);

        // Assertion Message
        String registerMessage = "Account was unexpectedly 'created' when given existing credentials";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, registerMessage);
    }

    @Test
    public void registerInvalidInfoTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = AccountManager.registerAccount(username, "");

        // Assertion Message
        String registerMessage = "Account was unexpectedly 'created' when given invalid credentials";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, registerMessage);
    }

    @Test
    public void loginTest() {
        // Setup
        AccountManager.logoutAccount(sessionID);

        // Action
        ResponseEntity<Object> responseEntity = AccountManager.loginAccount(username, password);
        sessionID = new SessionID(((JSONObject) Objects.requireNonNull(responseEntity.getBody())).get("sessionID").toString());

        // Assertion Message
        String loginMessage = "Could not login an account with valid credentials";

        // Assertion Statement
        Assertions.assertTrue(AccountManager.accountsRepo.existsById(Objects.requireNonNull(AccountManager.verifySession(sessionID)).getID()), loginMessage);
    }

    @Test
    public void loginAlreadyLoggedInTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = AccountManager.loginAccount(username, password);

        // Assertion Message
        String loginMessage = "Account was unexpectedly 'logged in' when given credentials of an already logged in account";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.UNAUTHORIZED, loginMessage);
    }

    @Test
    public void loginInvalidInfoTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = AccountManager.loginAccount(username, "");

        // Assertion Message
        String loginMessage = "Account was unexpectedly 'logged in' when given invalid credentials";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, loginMessage);
    }

    @Test
    public void loginAccountDoesNotExistTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = AccountManager.loginAccount("notUsername", password);

        // Assertion Message
        String loginMessage = "Account was unexpectedly 'logged in' when given credentials of account that doesn't exist";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND, loginMessage);
    }

    @Test
    public void logoutTest() {
        // Setup (not Required)

        // Action
        AccountManager.logoutAccount(sessionID);

        // Assertion Message
        String logoutMessage = "Could not logout an account with valid credentials";

        // Assertion Statement
        Assertions.assertNull(AccountManager.accountsRepo.findBySessionID(sessionID.getID()), logoutMessage);
    }

    @Test
    public void logoutAlreadyLoggedOutTest() {
        // Setup
        AccountManager.logoutAccount(sessionID);

        // Action
        ResponseEntity<Object> responseEntity = AccountManager.logoutAccount(sessionID);

        // Assertion Message
        String logoutMessage = "Account was unexpectedly 'logged out' when given credentials of an already logged in account";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, logoutMessage);
    }

    @Test
    public void logoutInvalidInfoTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = AccountManager.logoutAccount(new SessionID("invalidID"));

        // Assertion Message
        String logoutMessage = "Account was unexpectedly 'logged out' when given invalid credentials";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, logoutMessage);
    }

    @Test
    public void logoutAccountDoesNotExistTest() {
        // Setup (Not Required)

        // Action
        ResponseEntity<Object> responseEntity = AccountManager.logoutAccount(new SessionID("diajmdlanmdln80"));

        // Assertion Message
        String logoutMessage = "Account was unexpectedly 'logged in' when given credentials of account that doesn't exist";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, logoutMessage);
    }

    @Test
    public void deleteTest() {
        // Setup (not Required)

        // Action
        AccountManager.deleteAccount(sessionID);

        // Assertion Message
        String deleteMessage = "Could not delete an account with valid credentials";

        // Assertion Statement
        Assertions.assertNull(AccountManager.accountsRepo.findBySessionID(sessionID.getID()), deleteMessage);
    }

    @Test
    public void deleteUnauthorizedTest() {
        // Setup
        AccountManager.logoutAccount(sessionID);

        // Action
        ResponseEntity<Object> responseEntity = AccountManager.deleteAccount(sessionID);

        // Assertion Message
        String deleteMessage = "Account was unexpectedly 'deleted' when given unauthorized credentials";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, deleteMessage);
    }

    @Test
    public void deleteInvalidInfoTest() {
        // Setup (Non Required)

        // Action
        ResponseEntity<Object> responseEntity = AccountManager.logoutAccount(new SessionID("invalidID"));

        // Assertion Message
        String deleteMessage = "Account was unexpectedly 'deleted' when given invalid credentials";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, deleteMessage);
    }

    @Test
    public void deleteAccountDoesNotExistTest() {
        // Setup (Not Required)

        // Action
        ResponseEntity<Object> responseEntity = AccountManager.logoutAccount(new SessionID("diajmdlanmdln80"));

        // Assertion Message
        String deleteMessage = "Account was unexpectedly 'delete' when given credentials of account that doesn't exist";

        // Assertion Statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, deleteMessage);
    }

    @Test
    public void getAccountIDByUsernameTest() {
        // Setup
        AccountID expectedAccountID = AccountManager.verifySession(sessionID);

        // Action
        AccountID actualAccountID = AccountManager.getAccountIDByUsername(username);

        // Assertion Message
        String getAccountIDMessage = "The actual AccountID did not match the expected AccountID";

        // Assertion Statement
        assert expectedAccountID != null;
        assert actualAccountID != null;
        Assertions.assertEquals(expectedAccountID.getID(), actualAccountID.getID(), getAccountIDMessage);
    }

    @Test
    public void getAccountIDByInvalidUsernameTest() {
        // Setup (Not required)

        // Action
        AccountID actualAccountID = AccountManager.getAccountIDByUsername("9");

        // Assertion Message
        String getAccountIDMessage = "The actual AccountID did not match the expected AccountID";

        // Assertion Statement
        Assertions.assertNull(actualAccountID, getAccountIDMessage);
    }
}
