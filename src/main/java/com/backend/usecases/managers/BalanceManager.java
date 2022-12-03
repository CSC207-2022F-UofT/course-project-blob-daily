package com.backend.usecases.managers;

import com.backend.entities.Pet;
import com.backend.repositories.PetRepo;
import com.backend.usecases.IErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Configurable
public class BalanceManager {
    private final PetRepo petRepo;
    private final IErrorHandler errorHandler;

    @Autowired
    public BalanceManager(PetRepo petRepo, IErrorHandler errorHandler) {
        this.petRepo = petRepo;
        this.errorHandler = errorHandler;
    }

    /**
     * Get request of balance from the database through the pet object
     * @param accountID string that represents the current session and verifies the action
     */
    public Double getBalance(String accountID){
        Optional<Pet> pet = this.petRepo.findById(accountID);

        if (pet.isEmpty()) {
            this.errorHandler.logError(new Exception("Pet object is empty"));
            return null;
        }

        return pet.get().getBalance();
    }

    /**
     * Post request of balance changed with the parameter amount
     * @param accountID string that represents the current session and verifies the action
     * @param amount the double that represents the amount added to the pet's balance
     */
    public boolean updateBalance(String accountID, double amount){
        Optional<Pet> pet = this.petRepo.findById(accountID);
        if (pet.isEmpty()) {
            this.errorHandler.logError(new Exception("Pet object is empty"));
            return false;
        }

        double updatedBalance = pet.get().getBalance() + amount;

        Pet updatedPet = new Pet(accountID, pet.get().getHealth(), updatedBalance, pet.get().getInventory(), pet.get().getCurrentOutfit());
        this.petRepo.save(updatedPet);
        return true;
    }
}
