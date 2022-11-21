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

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    @Test
    public void testPet() {
        ArrayList<ShopItem> itemList = new ArrayList<>();
        itemList.add(new ShopItem("1234567890", 19.90, "Pants", "Classic pair of white pants"));


        Pet pet = new Pet("1234567890", 19.90, 59.90, itemList, itemList);

        Assertions.assertEquals(pet.getID(), "1234567890");
        Assertions.assertEquals(pet.getHealth(), 19.90, 0);
        Assertions.assertEquals(pet.getBalance(), 59.90, 0);
        Assertions.assertEquals(pet.getInventory(), itemList);
        Assertions.assertEquals(pet.getCurrentOutfit(), itemList);
    }
}
