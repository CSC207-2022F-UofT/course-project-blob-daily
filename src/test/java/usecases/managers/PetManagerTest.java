package usecases.managers;


import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Pet;
import com.backend.entities.ShopItem;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.PetManager;
import com.backend.usecases.managers.ShopManager;
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
public class PetManagerTest {
    private SessionID sessionID;
    private AccountID accountID;
    private Pet pet;
    private final PetManager petManager;
    private final ShopManager shopManager;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;


    @Autowired
    public PetManagerTest(PetManager petManager, ShopManager shopManager, AccountSystemFacade accountSystemFacade, AccountManager accountManager){
        this.petManager = petManager;
        this.shopManager = shopManager;
        this.accountSystemFacade = accountSystemFacade;
        this.accountManager = accountManager;
    }

    @BeforeEach
    public void shopTestSetup(){
        String username = "reeeethegreat";
        String password = "abc123!";
        ResponseEntity<Object> register = this.accountSystemFacade.registerAccount(username, password);
        if (!(register.getStatusCode() == HttpStatus.OK)){
            sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(username, password).getBody())).get("sessionID"));
        } else  {
            sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(register.getBody())).get("sessionID"));
        }

        accountID = this.accountManager.getAccountIDByUsername("reeeethegreat");
        Assertions.assertNotNull(accountID);
        pet = petManager.initializePet(accountID.getID());

    }

    @AfterEach
    public void tearDown() {
        if (sessionID != null) {
            this.accountSystemFacade.logoutAccount(this.sessionID);
        }

    }

    @Test
    public void getPetTest(){
        // Action
        Pet expectedPet = petManager.getPet(accountID.getID());

        // Assertion Message
        String addPetMessage = "The given sessionID to add Pet is invalid";

        // Assertion Statement
        Assertions.assertEquals(expectedPet.getID(), pet.getID(),  addPetMessage);
    }

    @Test
    public void initializePet(){
        // Value
        ArrayList<ShopItem> curInventory = new ArrayList<>();
        curInventory.add(new ShopItem("1480775928", 19.9, "brown pants", "a pair of classic fit brown pants", "", 2));
        Pet expected = new Pet("1234567890", 85.00, 25.4, curInventory, new ArrayList<>());

        // Action
        Pet actual = petManager.initializePet("1234567890");

        // Assertion Message
        String addPetMessage = "The given sessionID to add Pet is invalid";

        // Assertion Statement
        Assertions.assertEquals(expected.getID(), actual.getID(),  addPetMessage);
    }

    @Test
    public void addPetFailed(){
        // Action
        Pet actual = petManager.initializePet("");

        // Assertion Message
        String addPetFailedMessage = "The given sessionID to add Pet is valid";

        // Assertion Statement
        Assertions.assertNull(actual, addPetFailedMessage);
    }

    @Test
    public void checkNotInInventoryTest(){
        // Value
        boolean expected = false;

        // Action
        ShopItem shopItem = shopManager.getShopItem("");
        boolean actual = petManager.checkInventory(accountID, shopItem);

        // Assertion Message
        String failedMessage = "The given check inventory Test failed";

        // Assertion Statement
        Assertions.assertEquals(expected, actual, failedMessage);
    }

    @Test
    public void deletePetTest(){

        // Action
        petManager.deletePet(accountID.getID());
        Pet pet = petManager.getPet(accountID.getID());
        boolean actual = pet == null;

        // Assertion Statement
        Assertions.assertTrue(actual);

    }

    @Test
    public void deletePetInvalidTest(){

        // Action
        petManager.deletePet("");
        Pet pet = petManager.getPet(accountID.getID());
        boolean actual = pet == null;

        // Assertion Statement
        Assertions.assertFalse(actual);

    }
}
