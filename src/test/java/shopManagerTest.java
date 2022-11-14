import backend.entities.ShopItem;
import backend.useCases.shopManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;


public class shopManagerTest {


    @Test
    public void testParser(){
        shopManager sm = new shopManager();
        HashMap shopObject = sm.hashmapConverter("{name: Boaz}");
        String name = (String) shopObject.get("name");
        System.out.print(shopObject.get("name"));
        Assertions.assertEquals(name, "Boaz");
    }

    @Test
    public void getShopItem(){
        shopManager sm = new shopManager();
        List<ShopItem> shopItems = sm.getShopItems();
        double cost = shopItems.get(0).getCost();
        Assertions.assertEquals(cost, 13.2, 0);
    }


}
