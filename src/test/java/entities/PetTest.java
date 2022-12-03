package entities;

import com.backend.entities.Pet;
import com.backend.entities.ShopItem;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PetTest {

    private final ArrayList<ShopItem> itemList = new ArrayList<>();
    private Pet pet;

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;

        itemList.add(new ShopItem("1234567890", 19.90, "Pants", "Classic pair of white pants", "", 2));
        pet = new Pet("1234567890", 19.90, 59.90, itemList, itemList);
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }


    @Test
    public void testPetGetID() {
        //Value
        String expected = "1234567890";

        // Action
        String actual = pet.getID();

        // Assertion Message
        String verifyTestID= "The given invalid id is invalid";

        // Assertion Statement
        Assertions.assertEquals(expected, actual, verifyTestID);
    }

    @Test
    public void testPetHealth() {
        //Value
        double expected = 19.90;

        // Action
        double actual = pet.getHealth();

        // Assertion Message
        String verifyTestHealth = "The given invalid health is invalid";

        // Assertion Statement
        Assertions.assertEquals(expected, actual, verifyTestHealth);
    }

    @Test
    public void testPetBalance() {
        //Value
        double expected = 59.90;

        // Action
        double actual = pet.getBalance();

        // Assertion Message
        String verifyTestBalance = "The given invalid balance is invalid";

        // Assertion Statement
        Assertions.assertEquals(expected, actual, verifyTestBalance);
    }

    @Test
    public void testPetInventory() {
        // Setup (Not required)

        // Action
        ArrayList<ShopItem> actual = pet.getInventory();

        // Assertion Message
        String verifyTestInventory = "The given inventory is invalid";

        // Assertion Statement
        Assertions.assertEquals(itemList, actual, verifyTestInventory);
    }

    @Test
    public void testPetCurrentOutfit() {
        // Setup (Not required)

        // Action
        ArrayList<ShopItem> actual = pet.getCurrentOutfit();

        // Assertion Message
        String verifyTestOutfit = "The given current Outfit is invalid";

        // Assertion Statement
        Assertions.assertEquals(itemList, actual, verifyTestOutfit);
    }
}