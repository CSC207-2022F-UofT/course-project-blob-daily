package entities;

import com.backend.entities.ShopItem;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShopItemTest {
    private final ShopItem shopItem = new ShopItem("1234567890", 19.90, "Pants", "Classic pair of white pants", "", 2);

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    @Test
    public void testShopItemID() {
        //Value
        String expected = "1234567890";

        // Action
        String actual = shopItem.getID();

        // Assertion Message
        String verifyShopItemID = "The given invalid balance is invalid";

        // Assertion Statement
        Assertions.assertEquals(expected, actual, verifyShopItemID );
    }

    @Test
    public void testShopItemCost() {
        //Value
        Double expected = 19.90;

        // Action
        Double actual = shopItem.getCost();

        // Assertion Message
        String verifyShopItemCost = "The given invalid balance is invalid";

        // Assertion Statement
        Assertions.assertEquals(expected, actual, verifyShopItemCost);
    }

    @Test
    public void testShopItemName() {
        //Value
        String expected = "Pants";

        // Action
        String actual = shopItem.getName();

        // Assertion Message
        String verifyShopItemName = "The given Item Name is invalid";

        // Assertion Statement
        Assertions.assertEquals(expected, actual, verifyShopItemName);
    }

    @Test
    public void testShopItemDescription() {
        //Value
        String expected = "Classic pair of white pants";

        // Action
        String actual = shopItem.getDescription();

        // Assertion Message
        String verifyShopDescription = "The given invalid description is invalid";

        // Assertion Statement
        Assertions.assertEquals(expected, actual, verifyShopDescription);
    }

    @Test
    public void testShopItemLink(){
        //Value
        String expected = "";

        // Action
        String actual = shopItem.getImageLink();

        // Assertion Message
        String verifyShopDescription = "The given invalid description is invalid";

        // Assertion Statement
        Assertions.assertEquals(expected, actual, verifyShopDescription);
    }

    @Test
    public void testShopItemLocation(){
        //Value
        int expected = 2;

        // Action
        int actual = shopItem.getLocation();

        // Assertion Message
        String verifyShopDescription = "The given invalid description is invalid";

        // Assertion Statement
        Assertions.assertEquals(expected, actual, verifyShopDescription);
    }
}

