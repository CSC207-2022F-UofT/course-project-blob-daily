package entities;

import com.backend.entities.ShopItem;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShopItemTest {
    private final ShopItem shopItem = new ShopItem("1234567890", 19.90, "Pants", "Classic pair of white pants");

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
        Assertions.assertEquals(shopItem.getID(), "1234567890");
    }

    @Test
    public void testShopItemCost() {
        Assertions.assertEquals(shopItem.getCost(), 19.90, 0);
    }

    @Test
    public void testShopItemName() {
        Assertions.assertEquals(shopItem.getName(), "Pants");
    }

    @Test
    public void testShopItemDescription() {
        Assertions.assertEquals(shopItem.getDescription(), "Classic pair of white pants");
    }
}

