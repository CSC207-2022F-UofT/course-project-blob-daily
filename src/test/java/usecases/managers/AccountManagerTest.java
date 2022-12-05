package usecases.managers;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.Account;
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

import java.util.Date;
import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountManagerTest {
    private final AccountsRepo accountsRepo;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;
    private SessionID sessionID;
    private final String username = "username";
    private final String password = "abc123!";

    @Autowired
    public AccountManagerTest(AccountsRepo accountsRepo, AccountSystemFacade accountSystemFacade, AccountManager accountManager) {
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
    public void verifySessionTest() {
        // Action
        String actualAccountID = Objects.requireNonNull(this.accountManager.verifySession(this.sessionID)).getID();

        // Assertion Message
        String verifySessionMessage = String.format("The given valid ID returned an unexpected accountID '%s'",
                actualAccountID);

        // Assertion Statement
        Assertions.assertTrue(this.accountsRepo.existsById(actualAccountID), verifySessionMessage);
    }

    @Test
    public void verifySessionInvalidTest() {
        // Values
        String invalidSessionID = "deal28check";

        // Action
        Object actual = this.accountManager.verifySession(new SessionID(invalidSessionID));

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
        boolean resultProtectedAccount = this.accountManager.verifyAccountInfo(protectedAccount);

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
        boolean resultAccountByAccountID = this.accountManager.verifyAccountInfo(accountByAccountID);

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
        boolean resultAccountBySessionID = this.accountManager.verifyAccountInfo(accountBySessionID);

        // Assertion Message
        String verifyAccountInfoMessage = "The given valid account returned invalid (false) unexpectedly";

        // Assertion Statement
        Assertions.assertTrue(resultAccountBySessionID, verifyAccountInfoMessage);
    }

    @Test
    public void verifyInvalidAccountInfoTest() {
        // Values
        ProtectedAccount protectedAccount = new ProtectedAccount(username, null);

        // Action
        boolean resultProtectedAccount = this.accountManager.verifyAccountInfo(protectedAccount);

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
        boolean resultAccountByAccountID = this.accountManager.verifyAccountInfo(accountByAccountID);

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
        boolean resultAccountBySessionID = this.accountManager.verifyAccountInfo(accountBySessionID);

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
        String hashedPassword = this.accountManager.hash(password);

        // Assertion Message
        String hashMessage = "The given string was not hashed correctly according to the SHA-256 algorithm";

        // Assertion Statement
        Assertions.assertEquals(hashedPassword, expectedHash, hashMessage);
    }

    @Test
    public void getAccountInfoTest() {
        // Values
        AccountID accountID = this.accountManager.verifySession(sessionID);
        ProtectedAccount expectedAccount = new ProtectedAccount(username, null);

        // Action
        ProtectedAccount actualAccount = this.accountManager.getAccountInfo(accountID);

        // Assertion Message
        String accountInfoMessage = "The given accountID didn't yield the corresponding account information";

        // Assertion Statement
        assert actualAccount != null;
        Assertions.assertEquals(expectedAccount.getUsername(), actualAccount.getUsername(), accountInfoMessage);
    }

    @Test
    public void getAccountIDByUsernameTest() {
        // Setup
        AccountID expectedAccountID = this.accountManager.verifySession(sessionID);

        // Action
        AccountID actualAccountID = this.accountManager.getAccountIDByUsername(username);

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
        AccountID actualAccountID = this.accountManager.getAccountIDByUsername("9");

        // Assertion Message
        String getAccountIDMessage = "The actual AccountID did not match the expected AccountID";

        // Assertion Statement
        Assertions.assertNull(actualAccountID, getAccountIDMessage);
    }

    @Test
    public void createAccountTest() {
        // Setup (Not required)

        // Action
        Account actualAccount = this.accountManager.createAccount(username, password);

        // Assertion Message
        String createAccountMessage = "The actual Account did not match the expected Account";

        // Assertion Statement
        Assertions.assertNotNull(actualAccount.getAccountID(), createAccountMessage);
        Assertions.assertNotNull(actualAccount.getSessionID(), createAccountMessage);
        Assertions.assertEquals(username, actualAccount.getUsername(), createAccountMessage);
        Assertions.assertEquals(password, actualAccount.getPassword(), createAccountMessage);
    }

    @Test
    public void hashPasswordTest() {
        // Setup
        String expectedHash = this.accountManager.hash(password);

        // Action
        Account actualAccount = new Account(new SessionID(null), username, password);
        this.accountManager.hashPassword(actualAccount, password);

        // Assertion Message
        String hashPasswordMessage = "The actual Hash did not match the expected Hash in the given account";

        // Assertion Statement
        Assertions.assertEquals(expectedHash, actualAccount.getPassword(), hashPasswordMessage);
    }

    @Test
    public void accountExistsTrueTest() {
        // Setup (Not Required)

        // Action
        boolean result = this.accountManager.accountExists(new Account(sessionID, username, password));

        // Assertion Message
        String accountExistsMessage = "Unexpectedly could not find a existent account";

        // Assertion Statement
        Assertions.assertTrue(result, accountExistsMessage);
    }

    @Test
    public void accountExistsFalseTest() {
        // Setup (Not Required)

        // Action
        boolean result = this.accountManager.accountExists(new Account(sessionID, "notAUsername", password));

        // Assertion Message
        String accountExistsMessage = "Unexpectedly could find a non-existent account";

        // Assertion Statement
        Assertions.assertFalse(result, accountExistsMessage);
    }

    @Test
    public void updateAccountTest() {
        // Setup
        SessionID newSessionID = new SessionID(null);
        newSessionID.generateID();
        Account expectedAccount = new Account(newSessionID, "testingPurpose", password);

        // Action
        this.accountManager.updateAccount(expectedAccount);

        // Assertion Message
        String updateAccountMessage = "Unexpectedly could not update the database with the new account information";

        // Assertion Statement
        Assertions.assertTrue(this.accountManager.accountExists(expectedAccount), updateAccountMessage);

        // Special Tear-Down
        this.accountSystemFacade.deleteAccount(newSessionID);
    }

    @Test
    public void validateCredentialsTest() {
        // Setup (Not Required)

        // Action
        Account resultantAccount = this.accountManager.validateCredentials(username, this.accountManager.hash(password));

        // Assertion Message
        String validateCredentialsMessage = "Unexpectedly could not find matching/valid credentials the database";

        // Assertion Statement
        Assertions.assertNotNull(resultantAccount, validateCredentialsMessage);
    }

    @Test
    public void validateCredentialsInvalidTest() {
        // Setup (Not Required)

        // Action
        Account resultantAccount = this.accountManager.validateCredentials("invalidUsername", password);

        // Assertion Message
        String validateCredentialsMessage = "Unexpectedly could find matching/valid credentials the database";

        // Assertion Statement
        Assertions.assertNull(resultantAccount, validateCredentialsMessage);
    }

    @Test
    public void getAccountTest() {
        // Setup (Not Required)

        // Action
        Account resultantAccount = this.accountManager.getAccount(this.accountManager.verifySession(sessionID));

        // Assertion Message
        String getAccountMessage = "Unexpectedly could not find matching/valid account in the database";

        // Assertion Statement
        Assertions.assertEquals(username, resultantAccount.getUsername(), getAccountMessage);
        Assertions.assertEquals(this.accountManager.hash(password), resultantAccount.getPassword(), getAccountMessage);
        Assertions.assertEquals(sessionID.getID(), resultantAccount.getSessionID(), getAccountMessage);
    }

    @Test
    public void getAccountInvalidTest() {
        // Setup (Not Required)

        // Action
        Account resultantAccount = this.accountManager.getAccount(new AccountID("invalidID"));

        // Assertion Message
        String getAccountMessage = "Unexpectedly could find matching/valid account in the database";

        // Assertion Statement
        Assertions.assertNull(resultantAccount, getAccountMessage);
    }
}
