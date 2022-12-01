package com.backend.usecases.managers;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Pet;
import com.backend.entities.ShopItem;
import com.backend.repositories.PetRepo;
import com.backend.repositories.ShopItemRepo;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Configurable
public class ShopManager {
    public static ShopItemRepo shopRepo;
    public static PetRepo petRepo;
    public ShopManager(ShopItemRepo shopRepo, PetRepo petRepo) {
        ShopManager.shopRepo = shopRepo;
        ShopManager.petRepo = petRepo;
    }

    /**
     * Adds the default pet for an account
     * @param id AccountId string that represents the pet linking to the account
     */
    public static Pet addPet(String id){
        ArrayList<ShopItem> curInventory = new ArrayList<>();
        curInventory.add(new ShopItem("124", 59.90, "hoodie", "provides warmth"));
        Pet pet = new Pet(id, 85.00, 0.0, curInventory, new ArrayList<>());
        ShopManager.petRepo.save(pet);
        return pet;
    }

    /**
     * Get request to get all shop items
     *
     */
    public static ArrayList<ShopItem> getShopItems() {
        return (ArrayList<ShopItem>) ShopManager.shopRepo.findAll();
    }

    /**
     * Get request to return the pet object and it's attributes
     * @param sessionID string that represents the current session and verifies the action
     */
    public static Optional<Pet> getPet(String sessionID){
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));

        if (curAccount == null){
            return Optional.empty();
        }
        return ShopManager.petRepo.findById(curAccount.getID());
    }

    /**
     * Post request of the pet object after modifying it with the newOutfit
     * @param sessionID string that represents the current session and verifies the action
     * @param newOutfit ArrayList of shopItem that would represent which items the pet is wearing
     */
    public static boolean updateCurrentOutfit(String sessionID, ArrayList<ShopItem> newOutfit){
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));
        if (curAccount == null){
            return false;
        }
        Optional<Pet> pet = ShopManager.petRepo.findById(curAccount.getID());
        if (pet.isEmpty()){
            return false;
        }

        Pet updatedPet = new Pet(curAccount.getID(), pet.get().getHealth(), 0.0, pet.get().getInventory(), newOutfit);
        ShopManager.petRepo.save(updatedPet);
        return true;
    }

    /**
     * Get request of balance from the database through the pet object
     * @param sessionID string that represents the current session and verifies the action
     *
     */
    public static Optional<Double> getBalance(String sessionID){
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));
        if (curAccount == null){
            return Optional.empty();
        }

        Optional<Pet> pet = ShopManager.petRepo.findById(curAccount.getID());

        return pet.map(Pet::getBalance);

    }

    /**
     * Post request of balance added with the parameter amount
     * @param sessionID string that represents the current session and verifies the action
     * @param amount the double that represents the amount added to the pet's balance
     */
    public static boolean addBalance(String sessionID, double amount){
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));
        if (curAccount == null){
            return false;
        }
        Optional<Pet> pet = ShopManager.petRepo.findById(curAccount.getID());
        if (pet.isEmpty()) {
            return false;
        }

        double updatedBalance = pet.get().getBalance() + amount;

        Pet updatedPet = new Pet(curAccount.getID(), pet.get().getHealth(), updatedBalance, pet.get().getInventory(), pet.get().getCurrentOutfit());
        ShopManager.petRepo.save(updatedPet);
        return true;
    }

    /**
     * Delete the account associated with the account ID
     * @param curAccount an AccountID entity such that it identifies the pet with the corresponding id
     *
     */
    public static void deleteAccount(AccountID curAccount){
        ShopManager.petRepo.deleteById(curAccount.getID());
    }


    /**
     * Post request of the pet owning the item and subtracting from its balance the cost of the item
     * @param sessionID string that represents the current session and verifies the action
     * @param itemID string that represents which item the pet is attempting to access
     */
    public static boolean purchaseItem(String itemID, String sessionID) {
        AccountID curAccount = AccountManager.verifySession(new SessionID(sessionID));
        if (curAccount == null){
            return false;
        }
        Optional<Pet> pet = ShopManager.petRepo.findById(curAccount.getID());
        if (pet.isEmpty()){
            return false;
        }

        Optional<ShopItem> shopItem = ShopManager.shopRepo.findById(itemID);
        if (shopItem.isEmpty()){
            return false;
        }

        if(pet.get().getInventory().contains(shopItem.get()) || pet.get().getBalance() < shopItem.get().getCost()){
            return false;
        }

        double updatedBalance = pet.get().getBalance() - shopItem.get().getCost();
        ArrayList<ShopItem> updatedInventory = pet.get().getInventory();

        updatedInventory.add(shopItem.get());


        Pet updatedPet = new Pet(curAccount.getID(), pet.get().getHealth(), updatedBalance, updatedInventory, pet.get().getCurrentOutfit());
        ShopManager.petRepo.save(updatedPet);

        return true;

    }
}
