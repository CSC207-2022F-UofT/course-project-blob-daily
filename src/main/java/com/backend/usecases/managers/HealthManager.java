package com.backend.usecases.managers;

import com.backend.entities.Pet;
import com.backend.entities.TaskCompletionRecord;
import com.backend.error.exceptions.SessionException;
import com.backend.repositories.PetRepo;
import com.backend.usecases.IErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@Configurable
public class HealthManager {
    private final PetRepo petRepo;
    private final IErrorHandler errorHandler;

    @Autowired
    public HealthManager(PetRepo petRepo, IErrorHandler errorHandler) {
        this.petRepo = petRepo;
        this.errorHandler = errorHandler;
    }

    /**
     * Post request of health added with the parameter amount
     * @param accountID string that represents the current session and verifies the action
     * @param amount the double that represents the amount added to the pet's balance
     */
    public void addHealth(String accountID, double amount){
        Optional<Pet> pet = this.petRepo.findById(accountID);
        if (pet.isEmpty()) {
            this.errorHandler.logError(new SessionException("Pet is null since accountID was invalid"));
            return;
        }

        double updatedHealth = pet.get().getHealth() + amount;

        Pet updatedPet = new Pet(accountID, updatedHealth, pet.get().getBalance(), pet.get().getInventory(), pet.get().getCurrentOutfit());
        this.petRepo.save(updatedPet);
    }

    /**
     * Decay of health overtime if task have not been completed
     *
     * @param accountID string that represents the current session and verifies the action
     */
    public void healthDecay(String accountID, List<TaskCompletionRecord> completionRecords){

        int length = completionRecords.size();
        if (length == 0){
            this.addHealth(accountID, -5);
            return;
        }
        TaskCompletionRecord lastTask =  completionRecords.get(length - 1);
        String time = lastTask.getTimestamp().toString();

        int lastCompletedYear = Integer.parseInt(time.substring(0, 4));
        int lastCompletedMonth =  Integer.parseInt(time.substring(5, 7));
        int lastCompletedDay =  Integer.parseInt(time.substring(8, 10));

        LocalDate localDate = LocalDate.now();
        int curYear = localDate.getYear();
        int curMonth = localDate.getMonthValue();
        int curDay = localDate.getDayOfMonth();

        if (curYear == lastCompletedYear && curMonth == lastCompletedMonth && curDay == lastCompletedDay){
            return;
        }

        Optional<Pet> pet = this.petRepo.findById(accountID);
        if (pet.isEmpty()){
            this.errorHandler.logError(new SessionException("Pet is null since accountID was invalid"));
            return;
        }

        this.addHealth(accountID, -5);
    }
}
