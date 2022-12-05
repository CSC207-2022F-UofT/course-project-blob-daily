package com.backend.usecases.managers;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.Pet;
import com.backend.entities.ShopItem;
import com.backend.repositories.PetRepo;
import com.backend.usecases.IErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Configurable
public class PetManager {
    private final PetRepo petRepo;
    private final IErrorHandler errorHandler;

    /**
     * Spring Boot Dependency Injection of the Accounts Repository
     * @param petRepo the dependency to be injected
     * @param errorHandler the dependency to be injected
     */
    @Autowired
    public PetManager(PetRepo petRepo, IErrorHandler errorHandler) {
        this.petRepo = petRepo;
        this.errorHandler = errorHandler;
    }

    /**
     * Return the pet object and it's attributes
     * @param accountID string that represents the current session and verifies the action
     */
    public Pet getPet(String accountID){
        Optional<Pet> pet = petRepo.findById(accountID);

        if (pet.isEmpty()){
            return null;
        }
        return pet.get();
    }


    /**
     * Adds the default pet for an account into the database
     * @param id AccountId string that represents the pet linking to the account
     */
    public Pet initializePet(String id){
        if (id.isEmpty()){
            return null;
        }
        ArrayList<ShopItem> curInventory = new ArrayList<>();
        ArrayList<ShopItem> curOutfit = new ArrayList<>();
        Pet pet = new Pet(id, 85.00, 325.0, curInventory, curOutfit);
        this.petRepo.save(pet);
        return pet;
    }

    /**
     * Delete the corresponding pet for the account
     * @param accountID string that represents the pet linking to the account
     */
    public void deletePet(String accountID){
        if (accountID == null){
            this.errorHandler.logError(new Exception("accountID is null thus account can't be deleted"));
            return;
        }

        this.petRepo.deleteById(accountID);
    }

    /**
     * Checks if given shop item is in the inventory
     * @param accountID string that represents the current account
     * @param shopItem ShopItem that with attributes of shopItem
     */
    public boolean checkInventory(AccountID accountID, ShopItem shopItem){

        Optional<Pet> pet = this.petRepo.findById(accountID.getID());

        if (pet.isEmpty()){
            this.errorHandler.logError(new Exception("Pet is empty"));
            return false;
        }

        if (shopItem == null){
            this.errorHandler.logError(new Exception("ShopItem is empty"));
            return false;
        }

        ArrayList<ShopItem> shopItems = pet.get().getInventory();
        for(ShopItem item : shopItems){
            if (item.getID().equals(shopItem.getID())){
                return true;
            }
        }
        //Need to use itemID to compare
        return false;
    }
}
