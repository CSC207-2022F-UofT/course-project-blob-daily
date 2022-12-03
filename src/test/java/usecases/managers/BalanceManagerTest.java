package usecases.managers;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.BalanceManager;
import com.backend.usecases.managers.PetManager;
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
public class BalanceManagerTest {
    private SessionID sessionID;
    private AccountID accountID;
    private final BalanceManager balanceManager;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;
    private final PetManager petManager;

    @Autowired
    public BalanceManagerTest(BalanceManager balanceManager, AccountSystemFacade accountSystemFacade, AccountManager accountManager, PetManager petManager) {
        this.balanceManager = balanceManager;
        this.accountSystemFacade = accountSystemFacade;
        this.accountManager = accountManager;
        this.petManager = petManager;
    }

    @BeforeEach
    public void balanceManagerTestSetup() {
        String username = "reeeethegreat";
        String password = "abc123!";
        ResponseEntity<Object> register = this.accountSystemFacade.registerAccount(username, password);
        if (!(register.getStatusCode() == HttpStatus.OK)) {
            sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(username, password).getBody())).get("sessionID"));
        } else {
            sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(register.getBody())).get("sessionID"));
        }

        accountID = this.accountManager.getAccountIDByUsername("reeeethegreat");
        Assertions.assertNotNull(accountID);

        this.petManager.initializePet(accountID.getID());
    }

    @AfterEach
    public void tearDown() {
        if (sessionID != null) {
            this.accountSystemFacade.logoutAccount(this.sessionID);
        }

    }

    @Test
    public void getBalanceTest(){

        //Value
        double expected = 25.4;

        //Action
        double actual = balanceManager.getBalance(accountID.getID());


        Assertions.assertEquals(expected, actual, 0);
    }

    @Test
    public void getInvalidBalanceTest(){

        //Value
        Double expected = null;

        //Action
        Double actual = balanceManager.getBalance("");


        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void updateBalanceTest(){
        boolean result = balanceManager.updateBalance(accountID.getID(), 10);

        //Value
        double expected = 35.4;

        //Action
        double actual = balanceManager.getBalance(accountID.getID());

        Assertions.assertEquals(expected, actual, 0.0001);
        Assertions.assertTrue(result);
    }

    @Test
    public void updateBalanceInvalidTest(){
        boolean result = balanceManager.updateBalance("", 10);

        Assertions.assertFalse(result);
    }

}
