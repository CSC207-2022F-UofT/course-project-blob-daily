package com.backend.usecases;

import com.backend.controller.PetController;
import com.backend.controller.ShopController;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.ItemID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Pet;
import com.backend.entities.ShopItem;
import com.backend.error.handlers.LogHandler;
import org.apache.juli.logging.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShopManager {

    public static void addShopItem(){

        ItemID ID = new ItemID(null);
        ID.generateID();
        ShopItem shopItem = new ShopItem(ID.getID(), 29.90, "brown boots", "a pair of boots that protects you from the snow");
        ShopController.shopRepo.save(shopItem);

    }

    public static void addPet(){
        ArrayList<ShopItem> curInventory = new ArrayList<>();
        curInventory.add(new ShopItem("124", 59.90, "hoodie", "provides warmth"));
        Pet pet = new Pet("9j(Gc5g)Id0G#Pt9s?De", 85.00, 0.0, curInventory, new ArrayList<>());
        PetController.petRepo.save(pet);
    }


    public static ArrayList<ShopItem> getShopItems() {
        return (ArrayList<ShopItem>) ShopController.shopRepo.findAll();
    }

    public static Optional<Pet> getPet(String sessionID){
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));

        //        if (pet == null){
//            LogHandler.logError(new Exception("pet is null"));
//        }
        assert curAccount != null;
        return PetController.petRepo.findById(curAccount.getID());
    }

    public static boolean updateCurrentOutfit(String sessionID, ArrayList<ShopItem> newOutfit){
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));
        assert curAccount != null;
        Optional<Pet> pet = PetController.petRepo.findById(curAccount.getID());
//        if (pet == null){
//            LogHandler.logError(new Exception("pet is null"));
//        }
        Pet updatedPet = new Pet(curAccount.getID(), pet.get().getHealth(), 0.0, pet.get().getInventory(), newOutfit);
        PetController.petRepo.save(updatedPet);
        return true;
    }

    public static double getBalance(String sessionID){
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));
        assert curAccount != null;
        Optional<Pet> pet = PetController.petRepo.findById(curAccount.getID());

        if (pet.isPresent())
        {
            return pet.get().getBalance();
        }else{
            LogHandler.logError(new Exception("pet does not exist"));
            return 0;
        }

    }

    public static void addBalance(String sessionID, double amount){
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));
        assert curAccount != null;
        Optional<Pet> pet = PetController.petRepo.findById(curAccount.getID());

        double updatedBalance = pet.get().getBalance() + amount;

        Pet updatedPet = new Pet(curAccount.getID(), pet.get().getHealth(), updatedBalance, pet.get().getInventory(), pet.get().getCurrentOutfit());
        PetController.petRepo.save(updatedPet);

    }

    public static boolean purchaseItem(String itemID, String sessionID) {
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));
        assert curAccount != null;
        Optional<Pet> pet = PetController.petRepo.findById(curAccount.getID());

        Optional<ShopItem> shopItem = ShopController.shopRepo.findById(itemID);

        double updatedBalance = pet.get().getBalance() - shopItem.get().getCost();
        ArrayList<ShopItem> updatedInventory = pet.get().getInventory();

        updatedInventory.add(shopItem.get());


        Pet updatedPet = new Pet(curAccount.getID(), pet.get().getHealth(), updatedBalance, updatedInventory, pet.get().getCurrentOutfit());
        PetController.petRepo.save(updatedPet);

        return true;
    }
}
