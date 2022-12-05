package usecases.facades;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Pet;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.facades.PetSystemFacade;
import com.backend.usecases.managers.AccountManager;
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
public class PetSystemFacadeTest {
    private SessionID sessionID;
    private AccountID accountID;
    private final PetSystemFacade petSystemFacade;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;
    private final PetManager petManager;

    @Autowired
    public PetSystemFacadeTest(PetSystemFacade petSystemFacade, AccountSystemFacade accountSystemFacade, AccountManager accountManager, PetManager petManager) {
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
        double expectedBalance = 305.1;

        //action
        petSystemFacade.purchaseItem(sessionID.getID(), "1480775937");
        Pet pet = this.petManager.getPet(accountID.getID());
        double actualBalance = pet.getBalance();

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
        String expectedResponse = "Item purchased";

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
        ResponseEntity<?> responseEntity = petSystemFacade.updateCurrentOutfit(sessionID, "1480775928");

        //assertion message
        String errorMessage = "Unable to update current outfit due to invalid session";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, errorMessage);
    }

    @Test
    public void updateCurrentOutfitFacadeInvalidTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.updateCurrentOutfit(new SessionID(""), "1480775928");

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
    public void getShopItemsFacadeTest(){
        //action
        ResponseEntity<?> responseEntity = petSystemFacade.getShopItems();

        //assertion message
        String errorMessage = "Unable to get ShopItems";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, errorMessage);
    }
}