package com.backend.controller;

import com.backend.entities.Pet;
import com.backend.repositories.PetRepo;
import com.backend.usecases.ShopManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PetController {

    public static PetRepo petRepo;

    public PetController(PetRepo petRepo) {
        PetController.petRepo = petRepo;
    }


    @GetMapping("/pet")
    public ResponseEntity<Object> pets(@RequestParam String sessionID){

        ShopManager.addPet();
        Optional<Pet> pet = ShopManager.getPet(sessionID);

        return new ResponseEntity<>(pet, HttpStatus.OK);
    }
}
