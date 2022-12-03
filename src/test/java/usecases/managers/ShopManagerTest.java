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
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShopManagerTest {
    private SessionID sessionID;
    private AccountID accountID;
    private final PetManager petManager;
    private final ShopManager shopManager;
    private final AccountManager accountManager;
    private final AccountSystemFacade accountSystemFacade;

    @Autowired
    public ShopManagerTest(ShopManager shopManager, PetManager petManager, AccountManager accountManager, AccountSystemFacade accountSystemFacade){
        this.shopManager = shopManager;
        this.petManager = petManager;
        this.accountManager = accountManager;
        this.accountSystemFacade = accountSystemFacade;
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
        petManager.initializePet(accountID.getID());

    }

    @AfterEach
    public void tearDown() {
        if (sessionID != null) {
            this.accountSystemFacade.logoutAccount(this.sessionID);
        }

    }


    @Test
    public void getShopItemsTest() {
        List<ShopItem> shopItems = shopManager.getShopItems();

        //Value
        String expected = "1480775928";

        // Action
        String actual = shopItems.get(0).getID();

        // Assertion Message
        String errorMessage = "The given Shop Item is invalid";

        // Assertion Statement
        Assertions.assertEquals(expected, actual, errorMessage);
    }

    @Test
    public void getShopItemTest(){
        //Value
        String expected = "1480775928";

        // Action
        ShopItem shopItem = shopManager.getShopItem("1480775928");
        String actual = shopItem.getID();

        // Assertion Message
        String errorMessage = "The given Shop Item is invalid";

        // Assertion Statement
        Assertions.assertEquals(expected, actual, errorMessage);
    }

    @Test
    public void updateCurrentOutfitTest(){
        ArrayList<ShopItem> newOutfit = new ArrayList<>();
        newOutfit.add(new ShopItem("2468135790", 19.90, "Boots", "Classic pair of white boots"));
        boolean result = shopManager.updateCurrentOutfit(accountID, newOutfit);
        Pet pet = petManager.getPet(accountID.getID());

        //Value
        String expected = "2468135790";

        // Action
        String actual = pet.getCurrentOutfit().get(0).getID();

        Assertions.assertEquals(expected, actual);
        Assertions.assertTrue(result);
    }

    @Test
    public void checkSufficientBalancePositiveTest(){
        // Value
        double expectedDifference = 8.5;

        // Action
        double actualDifference = shopManager.checkSufficientBalance(accountID, "3935521889");

        Assertions.assertEquals(expectedDifference, actualDifference);
    }

    @Test
    public void checkSufficientBalanceNegativeTest(){
        // Value
        double expectedDifference = -4.5;

        // Action
        double actualDifference = shopManager.checkSufficientBalance(accountID, "6843285467");

        Assertions.assertEquals(expectedDifference, actualDifference);
    }

    @Test
    public void updateInventoryTest(){
        shopManager.updateInventory(accountID.getID(), "3935521889");
        Pet pet = petManager.getPet(accountID.getID());

        //Value
        String expected = "3935521889";

        // Action
        int inventoryLength = pet.getInventory().size();
        String actual = pet.getInventory().get(inventoryLength - 1).getID();

        Assertions.assertEquals(expected, actual);
    }
}
