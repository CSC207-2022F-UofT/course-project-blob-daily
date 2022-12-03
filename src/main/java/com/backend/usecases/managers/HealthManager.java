package com.backend.usecases.managers;

import com.backend.entities.Pet;
import com.backend.entities.TaskCompletionRecord;
import com.backend.error.exceptions.SessionException;
import com.backend.repositories.PetRepo;
import com.backend.usecases.IErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Configurable
public class HealthManager {
    private final PetRepo petRepo;
    private final IErrorHandler errorHandler;

    /**
     * Spring Boot Dependency Injection of the Accounts Repository
     * @param petRepo the dependency to be injected
     * @param errorHandler the dependency to be injected
     */
    @Autowired
    public HealthManager(PetRepo petRepo, IErrorHandler errorHandler) {
        this.petRepo = petRepo;
        this.errorHandler = errorHandler;
    }

    /**
     * Changing health with the parameter amount for the corresponding account
     * @param accountID string that represents the current session and verifies the action
     * @param amount the double that represents the amount added to the pet's balance
     */
    public void updateHealth(String accountID, double amount){
        Optional<Pet> pet = this.petRepo.findById(accountID);
        if (pet.isEmpty()) {
            this.errorHandler.logError(new SessionException("Pet is null since accountID was invalid"));
            return;
        }
        double updatedHealth = pet.get().getHealth() + amount;
        if (updatedHealth > 100){
            updatedHealth = 100;
        }

        Pet updatedPet = new Pet(accountID, updatedHealth, pet.get().getBalance(), pet.get().getInventory(), pet.get().getCurrentOutfit());
        this.petRepo.save(updatedPet);
    }

    /**
     * Decay of health overtime if task have not been completed
     * @param accountID string that represents the current session and verifies the action
     */
    public void healthDecay(String accountID, List<TaskCompletionRecord> completionRecords){

        int length = completionRecords.size();
        if (length == 0){
            this.updateHealth(accountID, -5);
            return;
        }
        TaskCompletionRecord lastTask =  completionRecords.get(length - 1);
        String date = lastTask.getDate();
        String today = new Date(System.currentTimeMillis()).toString().substring(0, 10);

        if(date.equals(today)){
            return;
        }

        Optional<Pet> pet = this.petRepo.findById(accountID);
        if (pet.isEmpty()){
            this.errorHandler.logError(new SessionException("Pet is null since accountID was invalid"));
            return;
        }

        this.updateHealth(accountID, -5);
    }
}
