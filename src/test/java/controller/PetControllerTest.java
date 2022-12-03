package controller;

import com.backend.QuestPetsApplication;
import com.backend.controller.PetController;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.ShopItem;
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
public class PetControllerTest {

    private final PetController petController;
    private final AccountSystemFacade accountSystemFacade;
    private SessionID sessionID;

    @Autowired
    public PetControllerTest(PetController petController, AccountSystemFacade accountSystemFacade) {
        this.petController = petController;
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
    public void getPetControllerTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.OK;

        // Action
        ResponseEntity<Object> actualResponse = petController.getPet(sessionID);

        // Assertion Message
        String getPetMessage = "Unexpectedly unable to get Pet entity given valid sessionID";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), getPetMessage);
    }

    @Test
    public void updatePetOutfitControllerTest() {
        // Values
        HttpStatus expectedStatus = HttpStatus.OK;

        // Action
        ShopItem shopItem = new ShopItem("1234567890", 99.9, "Top Hat", "Testing item", "", 0);
        ResponseEntity<Object> actualResponse = petController.updatePetOutfit(sessionID, shopItem);

        // Assertion Message
        String updatePetOutfitMessage = "Unexpectedly unable to update Pet Outfit entity given valid sessionID and curOutfit";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), updatePetOutfitMessage);
    }

    @Test
    public void getPetBalanceController(){
        // Values
        HttpStatus expectedStatus = HttpStatus.OK;

        // Action
        ResponseEntity<Object> actualResponse = petController.getBalance(sessionID);

        // Assertion Message
        String getPetBalanceMessage = "Unexpectedly unable to get Pet balance given valid sessionID";

        // Assertion Statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), getPetBalanceMessage);
    }
}
