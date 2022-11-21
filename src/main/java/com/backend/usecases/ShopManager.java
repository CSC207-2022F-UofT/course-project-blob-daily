package com.backend.usecases;

import com.backend.controller.PetController;
import com.backend.controller.ShopController;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Pet;
import com.backend.entities.ShopItem;
import com.backend.error.handlers.LogHandler;

import java.util.ArrayList;
import java.util.Optional;

public class ShopManager {


    /**
     * Adds the default pet for an account onto the database
     * @param id AccountId string that represents the pet linking to the account
     */
    public static Pet addPet(String id){
        ArrayList<ShopItem> curInventory = new ArrayList<>();
        curInventory.add(new ShopItem("124", 59.90, "hoodie", "provides warmth"));
        Pet pet = new Pet(id, 85.00, 0.0, curInventory, new ArrayList<>());
        PetController.petRepo.save(pet);
        return pet;
    }

    /**
     * Get request to get all shop items from the database
     *
     */
    public static ArrayList<ShopItem> getShopItems() {
        return (ArrayList<ShopItem>) ShopController.shopRepo.findAll();
    }

    /**
     * Get request to return the pet object and it's attributes from the database
     * @param sessionID string that represents the current session and verifies the action
     */
    public static Optional<Pet> getPet(String sessionID){
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));

        assert curAccount != null;
        return PetController.petRepo.findById(curAccount.getID());
    }

    /**
     * Post request of the pet object after modifiying it with the newOutfit to the database
     * @param sessionID string that represents the current session and verifies the action
     * @param newOutfit ArrayList of shopItem that would represent which items the pet is wearing
     */
    public static boolean updateCurrentOutfit(String sessionID, ArrayList<ShopItem> newOutfit){
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));
        assert curAccount != null;
        Optional<Pet> pet = PetController.petRepo.findById(curAccount.getID());
        assert pet.isPresent();

        Pet updatedPet = new Pet(curAccount.getID(), pet.get().getHealth(), 0.0, pet.get().getInventory(), newOutfit);
        PetController.petRepo.save(updatedPet);
        return true;
    }

    /**
     * Get request of balance from the database through the pet object
     * @param sessionID string that represents the current session and verifies the action
     *
     */
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

    /**
     * Post request of balance added with the parameter amount from the database through the pet object
     * @param sessionID string that represents the current session and verifies the action
     * @param amount the double that represents the amount added to the pet's balance
     */
    public static void addBalance(String sessionID, double amount){
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));
        assert curAccount != null;
        Optional<Pet> pet = PetController.petRepo.findById(curAccount.getID());
        assert pet.isPresent();

        double updatedBalance = pet.get().getBalance() + amount;

        Pet updatedPet = new Pet(curAccount.getID(), pet.get().getHealth(), updatedBalance, pet.get().getInventory(), pet.get().getCurrentOutfit());
        PetController.petRepo.save(updatedPet);

    }

    /**
     * Delete the account associated with the account ID
     * @param curAccount an AccountID entity such that it identifies the pet with the corresponding id
     *
     */
    public static void deleteAccount(AccountID curAccount){
        PetController.petRepo.deleteById(curAccount.getID());
    }


    /**
     * Post request of the pet owning the item and subtracting from its balance the cost of the item to the database
     * @param sessionID string that represents the current session and verifies the action
     * @param itemID string that represents which item the pet is attempting to access
     */
    public static boolean purchaseItem(String itemID, String sessionID) {
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));
        assert curAccount != null;
        Optional<Pet> pet = PetController.petRepo.findById(curAccount.getID());
        assert pet.isPresent();

        Optional<ShopItem> shopItem = ShopController.shopRepo.findById(itemID);
        assert shopItem.isPresent();

        if(pet.get().getInventory().contains(shopItem.get()) || pet.get().getBalance() < shopItem.get().getCost()){
            return false;
        }

        double updatedBalance = pet.get().getBalance() - shopItem.get().getCost();
        ArrayList<ShopItem> updatedInventory = pet.get().getInventory();

        updatedInventory.add(shopItem.get());


        Pet updatedPet = new Pet(curAccount.getID(), pet.get().getHealth(), updatedBalance, updatedInventory, pet.get().getCurrentOutfit());
        PetController.petRepo.save(updatedPet);

        return true;

    }
}
