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

        itemList.add(new ShopItem("1234567890", 19.90, "Pants", "Classic pair of white pants"));
        pet = new Pet("1234567890", 19.90, 59.90, itemList, itemList);
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }


    @Test
    public void testPetGetID() {
        Assertions.assertEquals(pet.getID(), "1234567890");
    }

    @Test
    public void testPetHealth() {
        Assertions.assertEquals(pet.getHealth(), 19.90, 0);
    }

    @Test
    public void testPetBalance() {
        Assertions.assertEquals(pet.getBalance(), 59.90, 0);
    }

    @Test
    public void testPetInventory() {
        Assertions.assertEquals(pet.getInventory(), itemList);
    }

    @Test
    public void testPetCurrentOutfit() {
        Assertions.assertEquals(pet.getCurrentOutfit(), itemList);
    }
}
