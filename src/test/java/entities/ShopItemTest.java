package entities;

import com.backend.entities.ShopItem;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShopItemTest {

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    @Test
    public void testShopItem() {
        ShopItem shopItem = new ShopItem("1234567890", 19.90, "Pants", "Classic pair of white pants");

        Assertions.assertEquals(shopItem.getID(), "1234567890");
        Assertions.assertEquals(shopItem.getCost(), 19.90, 0);
        Assertions.assertEquals(shopItem.getName(), "Pants");
        Assertions.assertEquals(shopItem.getDescription(), "Classic pair of white pants");
    }


}

