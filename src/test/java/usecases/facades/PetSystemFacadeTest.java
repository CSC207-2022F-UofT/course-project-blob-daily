package usecases.facades;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.facades.PetSystemFacade;
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

import java.util.ArrayList;
import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetSystemFacadeTest {
    private SessionID sessionID;
    private AccountID accountID;
    private final BalanceManager balanceManager;
    private final PetSystemFacade petSystemFacade;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;
    private final PetManager petManager;

    @Autowired
    public PetSystemFacadeTest(BalanceManager balanceManager, PetSystemFacade petSystemFacade, AccountSystemFacade accountSystemFacade, AccountManager accountManager, PetManager petManager) {
        this.balanceManager = balanceManager;
        this.petSystemFacade = petSystemFacade;
        this.accountSystemFacade = accountSystemFacade;
        this.accountManager = accountManager;
        this.petManager = petManager;
    }

    @BeforeEach
    public void shopTestSetup() {
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
    public void purchaseItemTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.purchaseItem(sessionID.getID(), "9679464033");

        //assertion message
        String errorMessage = "Unable to purchase item due to invalid session";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, errorMessage);
    }

    @Test
    public void purchaseItemInvalidTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.purchaseItem("", "");

        //assertion message
        String errorMessage = "Unexpected to complete with invalid itemID";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, errorMessage);
    }

    @Test
    public void purchaseItemRemainingBalanceTest(){
        //expected
        double expectedBalance = 5.5;

        //action
        petSystemFacade.purchaseItem(sessionID.getID(), "9679464033");
        double actualBalance = balanceManager.getBalance(accountID.getID());

        //assertion message
        String errorMessage = "Incorrect remaining balance after item purchased";

        //assertion statement
        Assertions.assertEquals(expectedBalance, actualBalance, errorMessage);
    }

    @Test
    public void purchaseItemInsufficientBalanceTest(){
        //expected
        String expectedResponse = "Insufficient balance";

        //action
        ResponseEntity<?> responseEntity = petSystemFacade.purchaseItem(sessionID.getID(), "3221143063");
        String actualResponse = Objects.requireNonNull(responseEntity.getBody()).toString();

        //assertion message
        String errorMessage = "Incorrect item purchased result";

        //assertion statement
        Assertions.assertEquals(actualResponse, expectedResponse, errorMessage);
    }

    @Test
    public void purchaseItemAlreadyOwnedTest(){
        //expected
        String expectedResponse = "Item already owned";

        //action
        ResponseEntity<?> responseEntity = petSystemFacade.purchaseItem(sessionID.getID(), "1480775928");
        String actualResponse = Objects.requireNonNull(responseEntity.getBody()).toString();

        //assertion message
        String errorMessage = "Incorrect item purchased result";

        //assertion statement
        Assertions.assertEquals(actualResponse, expectedResponse, errorMessage);
    }

    @Test
    public void updateCurrentOutfitFacadeTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.updateCurrentOutfit(sessionID, new ArrayList<>());

        //assertion message
        String errorMessage = "Unable to update current outfit due to invalid session";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, errorMessage);
    }

    @Test
    public void updateCurrentOutfitFacadeInvalidTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.updateCurrentOutfit(new SessionID(""), new ArrayList<>());

        //assertion message
        String errorMessage = "Unexpected to complete update current outfit with invalid itemID";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, errorMessage);
    }

    @Test
    public void getPetFacadeTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.getPet(sessionID);

        //assertion message
        String errorMessage = "Unable to get pet due to invalid session";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, errorMessage);
    }

    @Test
    public void getPetFacadeInvalidTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.getPet(new SessionID(""));

        //assertion message
        String errorMessage = "Unexpected to complete get Pet with invalid sessionID";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, errorMessage);
    }

    @Test
    public void addHealthFacadeTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.addHealth(sessionID, 10);

        //assertion message
        String errorMessage = "Unable to add Health to invalid session";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, errorMessage);
    }

    @Test
    public void addHealthFacadeInvalidTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.addHealth(new SessionID(""), 10);

        //assertion message
        String errorMessage = "Unexpected to complete add Health with invalid sessionID";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, errorMessage);
    }

//    @Test
//    public void healthDecayFacadeTest(){
//        //action
//        ResponseEntity<?> responseEntity = petSystemFacade.healthDecay(sessionID);
//
//        //assertion message
//        String errorMessage = "Unable to check health decay to invalid session";
//
//        //assertion statement
//        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, errorMessage);
//    }
//
//    @Test
//    public void healthDecayFacadeInvalidTest(){
//        //action
//        ResponseEntity<?> responseEntity = petSystemFacade.healthDecay(new SessionID(""));
//
//        //assertion message
//        String errorMessage = "Unexpected to complete health decay with invalid sessionID";
//
//        //assertion statement
//        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, errorMessage);
//    }

    @Test
    public void getBalanceFacadeTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.getBalance(sessionID);

        //assertion message
        String errorMessage = "Unable to getBalance due to invalid session";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, errorMessage);
    }

    @Test
    public void getBalanceFacadeInvalidTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.getBalance(new SessionID(""));

        //assertion message
        String errorMessage = "Unexpected to complete getBalance with invalid sessionID";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, errorMessage);
    }

    @Test
    public void updateBalanceFacadeTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.updateBalance(sessionID, 10);

        //assertion message
        String errorMessage = "Unable to updateBalance due to invalid session";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, errorMessage);
    }

    @Test
    public void updateBalanceFacadeInvalidTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.updateBalance(new SessionID(""), 10);

        //assertion message
        String errorMessage = "Unexpected to complete updateBalance with invalid sessionID";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, errorMessage);
    }

    @Test
    public void getShopItemsFacadeTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.getShopItems();

        //assertion message
        String errorMessage = "Unable to get ShopItems";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, errorMessage);
    }
}