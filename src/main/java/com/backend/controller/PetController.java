package com.backend.controller;

import com.backend.entities.Pet;
import com.backend.entities.ShopItem;
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

        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @PostMapping("/addPet")
    public ResponseEntity<Object> addPet(@RequestParam String id){
        Pet pet = ShopManager.addPet(id);

        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @PostMapping("/updatePetOutfit")
    public ResponseEntity<Object> updatePetOutfit(@RequestParam String sessionID, ArrayList<ShopItem> newOutfit){
        boolean successful = ShopManager.updateCurrentOutfit(sessionID, newOutfit);

        return new ResponseEntity<>(successful, HttpStatus.OK);
    }

    @GetMapping("/petBalance")
    public ResponseEntity<Object> getBalance(@RequestParam String sesionId){
        Double balance = ShopManager.getBalance(sesionId);

        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @PostMapping("/addPetBalance")
    public ResponseEntity<Object> addPetBalance(@RequestParam String sessionID, Double balance){
        ShopManager.addBalance(sessionID, balance);

        return new ResponseEntity<>("Balance added", HttpStatus.OK);
    }

    @PostMapping("/purchaseItems")
    public ResponseEntity<Object> purchaseItem(@RequestParam String sessionID, String itemId){
        ShopManager.purchaseItem(itemId, sessionID);

        return new ResponseEntity<>("Item purchased", HttpStatus.OK);
    }


}
