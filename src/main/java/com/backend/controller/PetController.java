package com.backend.controller;

import com.backend.entities.Pet;
import com.backend.entities.ShopItem;
import com.backend.error.handlers.LogHandler;
import com.backend.repositories.PetRepo;
import com.backend.usecases.ShopManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class PetController {

    public static PetRepo petRepo;

    public PetController(PetRepo petRepo) {
        PetController.petRepo = petRepo;
    }


    /**
     * Get request to getPet with the sessionID from the database
     * @param sessionID string that represents the current session and verifies the action
     */
    @GetMapping("/pet")
    public ResponseEntity<Object> pets(@RequestParam String sessionID){
        Optional<Pet> pet = ShopManager.getPet(sessionID);

        if (pet.isPresent()){
            return new ResponseEntity<>(pet, HttpStatus.OK);
        }
        return LogHandler.logError(new Exception("curAccount is null"), HttpStatus.BAD_REQUEST);
    }

    /**
     * Post request to addPet with the petID from the database
     * @param id string that represents the current session and verifies the action
     */
    @PostMapping("/addPet")
    public ResponseEntity<Object> addPet(@RequestParam String id){
        Pet pet = ShopManager.addPet(id);

        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    /**
     * Post request to replace the current pet with the same pet with an updated outfit from the database
     * @param sessionID string that represents the current session and verifies the action
     * @param newOutfit ArrayList of ShopItems that will be for the updated outfit
     */
    @PostMapping("/updatePetOutfit")
    public ResponseEntity<Object> updatePetOutfit(@RequestParam String sessionID, ArrayList<ShopItem> newOutfit){
        boolean successful = ShopManager.updateCurrentOutfit(sessionID, newOutfit);

        if (successful){
            return new ResponseEntity<>("Pet outfit successfully updated", HttpStatus.OK);
        }

        return LogHandler.logError(new Exception("outfit failed to update"), HttpStatus.BAD_REQUEST);
    }

    /**
     * Get request to get Pet Balance with the sessionID from the database
     * @param sessionID string that represents the current session and verifies the action
     */
    @GetMapping("/petBalance")
    public ResponseEntity<Object> getBalance(@RequestParam String sessionID){
        Optional<Double> balance = ShopManager.getBalance(sessionID);

        if (balance.isPresent()){
            return new ResponseEntity<>(balance, HttpStatus.OK);
        }

        return LogHandler.logError(new Exception("Get balance failed"), HttpStatus.BAD_REQUEST);
    }

    /**
     * Post request to add balance to a pet with the sessionID in the database
     * @param sessionID string that represents the current session and verifies the action
     * @param balance double that represent amount that will be added
     */
    @PostMapping("/addPetBalance")
    public ResponseEntity<Object> addPetBalance(@RequestParam String sessionID, Double balance){
        boolean successful = ShopManager.addBalance(sessionID, balance);
        if (successful){
            return new ResponseEntity<>("Balance added", HttpStatus.OK);
        }

        return LogHandler.logError(new Exception("Add balance failed"), HttpStatus.BAD_REQUEST);
    }

    /**
     * Post request to have the pet own the item and reduce the balance accordingly to the cost of the item from the database
     * @param sessionID string that represents the current session and verifies the action
     * @param itemId string that represents the item to be purchased
     */
    @PostMapping("/purchaseItems")
    public ResponseEntity<Object> purchaseItem(@RequestParam String sessionID, String itemId){
        boolean successful = ShopManager.purchaseItem(itemId, sessionID);

        if (successful){
            return new ResponseEntity<>("Item purchased", HttpStatus.OK);
        }

        return LogHandler.logError(new Exception("Purchase item failed"), HttpStatus.BAD_REQUEST);

    }


}
