package com.backend.usecases.managers;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.Pet;
import com.backend.entities.ShopItem;
import com.backend.repositories.PetRepo;
import com.backend.repositories.ShopItemRepo;
import com.backend.usecases.IErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Configurable
public class ShopManager {


    private final ShopItemRepo shopRepo;
    private final PetRepo petRepo;
    private final IErrorHandler errorHandler;

    /**
     * Spring Boot Dependency Injection of the Accounts Repository
     * @param petRepo the dependency to be injected
     * @param errorHandler the dependency to be injected
     */
    @Autowired
    @SuppressWarnings("unused")
    public ShopManager(ShopItemRepo shopRepo, PetRepo petRepo, IErrorHandler errorHandler) {
        this.shopRepo = shopRepo;
        this.petRepo = petRepo;
        this.errorHandler = errorHandler;
    }

    /**
     * Returns all the shop items from the database through petRepo
     */
    public List<ShopItem> getShopItems() {
        return this.shopRepo.findAll();
    }

    /**
     * Returns a shop items from the database through petRepo
     * @param shopItemID string that represents the id of the shopItem
     */
    public ShopItem getShopItem(String shopItemID) {
        Optional<ShopItem> shopItem = this.shopRepo.findById(shopItemID);
        if (shopItem.isEmpty()){
            return null;
        }
        return shopItem.get();
    }


    /**
     * Update the pet object after modifying it with the newOutfit
     * @param accountID string that represents the user and the pet's identity
     * @param shopItem ShopItem that would represent which items the pet is wearing
     */
    public boolean updateCurrentOutfit(AccountID accountID, ShopItem shopItem){
        Optional<Pet> pet = this.petRepo.findById(accountID.getID());
        if (pet.isEmpty()) {
            return false;
        }

        ArrayList<ShopItem> curOutfit = pet.get().getCurrentOutfit();
        ArrayList<ShopItem> newOutfit = new ArrayList<>();

        boolean alreadyEquipped = false;

        for(ShopItem s: curOutfit){
            if (shopItem.getLocation() == s.getLocation()){
                newOutfit.add(shopItem);
                alreadyEquipped = true;
            }
            else{
                newOutfit.add(s);
            }
        }

        if(!alreadyEquipped){
            newOutfit.add(shopItem);
        }

        Pet updatedPet = new Pet(accountID.getID(), pet.get().getHealth(), pet.get().getBalance(), pet.get().getInventory(), newOutfit);
        this.petRepo.save(updatedPet);
        return true;
    }

    /**
     * Return the difference between the itemID and the balance of the pet
     * @param accountID string that represents the current session and verifies the action
     * @param itemID string that represents the corresponding shopItem
     */
    public double checkSufficientBalance(AccountID accountID, String itemID){

        Optional<Pet> pet = this.petRepo.findById(accountID.getID());
        if (pet.isEmpty()){
            this.errorHandler.logError(new Exception("pet is empty"));
            return 0;
        }
        double balance = pet.get().getBalance();

        ShopItem shopItem = this.getShopItem(itemID);
        if (shopItem == null){
            this.errorHandler.logError(new Exception("shopItem is empty"));
            return 0;
        }

        return balance - shopItem.getCost();
    }


    /**
     * Update the Inventory of the pet by adding the item from the itemID
     * @param accountID string that represents the current session and verifies the action
     * @param itemID string that represents the corresponding shopItem
     */
    public boolean updateInventory(String accountID, String itemID){
        Optional<Pet> pet = this.petRepo.findById(accountID);
        if (pet.isEmpty()){
            return false;
        }
        ArrayList<ShopItem> updatedInventory = pet.get().getInventory();

        Optional<ShopItem> shopItem = this.shopRepo.findById(itemID);
        if (shopItem.isEmpty()){
            return false;
        }

        updatedInventory.add(shopItem.get());

        Pet updatedPet = new Pet(accountID, pet.get().getHealth(), pet.get().getBalance(), updatedInventory, pet.get().getCurrentOutfit());
        this.petRepo.save(updatedPet);

        return true;
    }

}
