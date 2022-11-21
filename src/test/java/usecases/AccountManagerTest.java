package usecases;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.Account;
import com.backend.entities.users.ProtectedAccount;
import com.backend.usecases.AccountManager;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountManagerTest {

    private SessionID sessionID;
    private String username = "username";
    private String password = "abc123!";

    @BeforeEach
    public void setup() {
        ResponseEntity<Object> register = AccountManager.registerAccount(this.username, this.password);

        if (!(register.getStatusCode() == HttpStatus.OK)){
            sessionID = new SessionID((String) AccountManager.loginAccount(this.username, this.password).getBody());
        } else  {
            sessionID = new SessionID((String) register.getBody());
        }

    }

    @AfterEach
    public void tearDown() {
        AccountManager.logoutAccount(this.sessionID);
    }

    @Test
    public void verifySessionTest() {
        // Values
        String expectedAccountID = "1d_Ra5h^Xx7V}Bl6R?Du";

        // Action
        String actualAccountID = AccountManager.verifySession(this.sessionID).getID();

        // Assertion Message
        String verifySessionMessage = String.format("The given valid ID '%s' returned an unexpected accountID '%s'",
                expectedAccountID, actualAccountID);

        // Assertion Statement
        Assertions.assertEquals(expectedAccountID, actualAccountID, verifySessionMessage);
    }

    @Test
    public void verifySessionInvalidTest() {
        // Values
        String invalidSessionID = "dmal28hdjk";

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
        String username = "username";
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

        String username = "username";
        String password = "abc123!";

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

        String username = "username";
        String password = "abc123!";

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
        String username = "username";

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

        String username = "username";
        String password = "abc123!";

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

        String username = "username";
        String password = "abc123!";

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
        Assertions.assertEquals(expectedAccount.getUsername(), actualAccount.getUsername(), accountInfoMessage);
    }
}
