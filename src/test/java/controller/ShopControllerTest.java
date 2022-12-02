package controller;

import com.backend.QuestPetsApplication;
import com.backend.controller.ShopController;
import com.backend.entities.IDs.SessionID;
import com.backend.usecases.facades.AccountSystemFacade;
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
public class ShopControllerTest {

    private final ShopController shopController;
    private final AccountSystemFacade accountSystemFacade;

    private SessionID sessionID;

    @Autowired
    public ShopControllerTest(ShopController shopController, AccountSystemFacade accountSystemFacade) {
        this.shopController = shopController;
        this.accountSystemFacade = accountSystemFacade;
    }


    @BeforeEach
    public void setup() {
        String username = "reeeethegreat";
        String password = "abc123!";
        sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(username, password).getBody())).get("sessionID"));
    }

    @AfterEach
    public void tearDown() {
        this.accountSystemFacade.logoutAccount(sessionID);
    }

    @Test
    public void getShopItemsTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.OK;

        // Action
        ResponseEntity<Object> actualResponse = shopController.getShopItems();

        // Assertion Message
        String getShopItemMessage = "Unexpectedly unable to get Shop item entities";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), getShopItemMessage);
    }

    @Test
    public void purchaseItemControllerTest(){
        // Values
        HttpStatus expectedStatus = HttpStatus.OK;

        // Action
        ResponseEntity<Object> actualResponse = shopController.purchaseItem(sessionID.getID(), "1480775928");

        // Assertion Message
        String getPetBalanceMessage = "Unexpectedly unable to get Pet balance given valid sessionID";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), getPetBalanceMessage);
    }
}
