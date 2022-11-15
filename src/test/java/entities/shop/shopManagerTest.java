package entities.shop;

import backend.entities.ShopItem;
import backend.error.handlers.LogHandler;
import backend.useCases.shopManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class shopManagerTest {

    @BeforeEach
    public void setup() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }


    @Test
    public void testParser(){
        HashMap shopObject = shopManager.convert("{name: Boaz}");
        String name = (String) shopObject.get("name");
        Assertions.assertEquals(name, "Boaz");
    }

    @Test
    public void testParserList(){
        HashMap shopObject = shopManager.convert("{name: Boaz, inventory: [pants, t-shirts]}");
        ArrayList<String> inventory = (ArrayList<String>) shopObject.get("inventory");
        ArrayList<String> inventoryActual = new ArrayList<>( Arrays.asList("pants", "t-shirts"));
        Assertions.assertEquals(inventory, inventoryActual);
    }

    @Test
    public void getShopItemCost(){
        List<ShopItem> shopItems = shopManager.getShopItems();
        double cost = shopItems.get(0).getCost();
        Assertions.assertEquals(cost, 13.2, 0);
    }

    @Test
    public void getShopItemDescription(){
        List<ShopItem> shopItems = shopManager.getShopItems();
        String cost = shopItems.get(0).getDescription();
        Assertions.assertEquals(cost, "it's very comfortable");
    }

    @Test
    public void getBalance(){
        double balance = shopManager.getBalance("1234");
        Assertions.assertEquals(balance, 28.9, 0);
    }


}
