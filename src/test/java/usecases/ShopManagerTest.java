package usecases;


import com.backend.QuestPetsApplication;
import com.backend.entities.Pet;
import com.backend.entities.ShopItem;
import com.backend.usecases.ShopManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShopManagerTest {
    private final String sessionID = "1fFg0lCi8ROr7aGd";

    @BeforeEach
    public void shopTestSetup(){
        ArrayList<ShopItem> curInventory = new ArrayList<>();
        curInventory.add(new ShopItem("124", 59.90, "hoodie", "provides warmth"));
        Pet pet = new Pet("9U;Kk6E`Zf9M`Lm2c}Am", 85.00, 25.4, curInventory, new ArrayList<>());
        ShopManager.petRepo.save(pet);
    }


    @Test
    public void getShopItemsTest() {
        ArrayList<ShopItem> shopItems = ShopManager.getShopItems();
        ShopItem shopItem = shopItems.get(0);

        // Assertion Statement
        Assertions.assertEquals(shopItem.getID(), "1480775928");
        Assertions.assertEquals(shopItem.getCost(), 19.9, 0);
        Assertions.assertEquals(shopItem.getName(), "brown pants");
        Assertions.assertEquals(shopItem.getDescription(), "a pair of classic fit brown pants");
    }

    @Test
    public void getPetTest() {

        Optional<Pet> pet = ShopManager.getPet(sessionID);


        Assertions.assertTrue(pet.isPresent());

        // Assertion Statement
        pet.ifPresent(value -> Assertions.assertEquals(value.getID(), "9U;Kk6E`Zf9M`Lm2c}Am"));
        pet.ifPresent(value -> Assertions.assertEquals(value.getHealth(), 85, 0));
        pet.ifPresent(value -> Assertions.assertEquals(value.getBalance(), 25.4, 0));
        pet.ifPresent(value -> Assertions.assertEquals(value.getInventory().get(0).getName(), "hoodie"));
        pet.ifPresent(value -> Assertions.assertEquals(value.getInventory().get(0).getCost(), 59.9, 0));
    }

    @Test
    public void updateOutfitTest(){
        ArrayList<ShopItem> newOutfit = new ArrayList<>();
        newOutfit.add(new ShopItem("2468135790", 19.90, "Boots", "Classic pair of white boots"));
        ShopManager.updateCurrentOutfit(sessionID, newOutfit);
        Optional<Pet> pet = ShopManager.getPet(sessionID);
        Assertions.assertTrue(pet.isPresent());
        Assertions.assertEquals(pet.get().getCurrentOutfit().get(0).getID(), "2468135790");
    }

    @Test
    public void getBalanceTest(){
        Assertions.assertTrue(ShopManager.getBalance(sessionID).isPresent());
        double balance = ShopManager.getBalance(sessionID).get();
        Assertions.assertEquals(balance, 25.4, 0);

    }

    @Test
    public void addBalanceTest(){
        ShopManager.addBalance(sessionID, 10);
        Assertions.assertTrue(ShopManager.getBalance(sessionID).isPresent());
        double updatedBalance = ShopManager.getBalance(sessionID).get();
        ShopManager.addBalance(sessionID, -10);
        Assertions.assertEquals(updatedBalance, 35.4, 0.0001);
    }

    @Test
    public void purchaseTest(){
        ShopManager.purchaseItem("9679464033", sessionID);
        Assertions.assertTrue(ShopManager.getBalance(sessionID).isPresent());
        double updatedBalance = ShopManager.getBalance(sessionID).get();
        Optional<Pet> updatedPet = ShopManager.getPet(sessionID);

        ShopManager.addBalance(sessionID, 19.9);

        Assertions.assertTrue(updatedPet.isPresent());
        Assertions.assertEquals(updatedPet.get().getInventory().get(1).getName(), "white pants");
        Assertions.assertEquals(updatedBalance, 5.5, 0.0001);
    }

}
