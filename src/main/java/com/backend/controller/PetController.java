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


    @GetMapping("/pet")
    public ResponseEntity<Object> pets(@RequestParam String sessionID){
        Optional<Pet> pet = ShopManager.getPet(sessionID);

        if (pet.isPresent()){
            return new ResponseEntity<>(pet, HttpStatus.OK);
        }
        return LogHandler.logError(new Exception("curAccount is null"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/addPet")
    public ResponseEntity<Object> addPet(@RequestParam String id){
        Pet pet = ShopManager.addPet(id);

        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @PostMapping("/updatePetOutfit")
    public ResponseEntity<Object> updatePetOutfit(@RequestParam String sessionID, ArrayList<ShopItem> newOutfit){
        boolean successful = ShopManager.updateCurrentOutfit(sessionID, newOutfit);

        if (successful){
            return new ResponseEntity<>("Pet outfit successfully updated", HttpStatus.OK);
        }

        return LogHandler.logError(new Exception("outfit failed to update"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/petBalance")
    public ResponseEntity<Object> getBalance(@RequestParam String sesionId){
        Optional<Double> balance = ShopManager.getBalance(sesionId);

        if (balance.isPresent()){
            return new ResponseEntity<>(balance, HttpStatus.OK);
        }

        return LogHandler.logError(new Exception("Get balance failed"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/addPetBalance")
    public ResponseEntity<Object> addPetBalance(@RequestParam String sessionID, Double balance){
        boolean successful = ShopManager.addBalance(sessionID, balance);
        if (successful){
            return new ResponseEntity<>("Balance added", HttpStatus.OK);
        }

        return LogHandler.logError(new Exception("Add balance failed"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/purchaseItems")
    public ResponseEntity<Object> purchaseItem(@RequestParam String sessionID, String itemId){
        boolean successful = ShopManager.purchaseItem(itemId, sessionID);

        if (successful){
            return new ResponseEntity<>("Item purchased", HttpStatus.OK);
        }

        return LogHandler.logError(new Exception("Purchase item failed"), HttpStatus.BAD_REQUEST);

    }


}
