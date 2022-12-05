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

    /**
     * Spring Boot Dependency Injection of the Accounts Repository
     * @param petRepo the dependency to be injected
     * @param errorHandler the dependency to be injected
     */
    @Autowired
    public BalanceManager(PetRepo petRepo, IErrorHandler errorHandler) {
        this.petRepo = petRepo;
        this.errorHandler = errorHandler;
    }

    /**
     * Update balance changed with the parameter amount for the corresponding account
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
