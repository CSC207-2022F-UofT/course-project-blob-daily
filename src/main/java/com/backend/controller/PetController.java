package com.backend.controller;

import com.backend.entities.IDs.SessionID;
import com.backend.entities.ShopItem;
import com.backend.usecases.facades.PetSystemFacade;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class PetController {

    private final PetSystemFacade petSystemFacade;

    @Autowired
    public PetController(PetSystemFacade petSystemFacade){
        this.petSystemFacade = petSystemFacade;
    }

    /**
     * Get request to getPet with the sessionID from the database
     * @param sessionID string that represents the current session and verifies the action
     */
    @GetMapping("/pet")
    public ResponseEntity<Object> getPet(@RequestParam SessionID sessionID){
        return this.petSystemFacade.getPet(sessionID);
    }

    /**
     * Post request to replace the current pet with the same pet with an updated outfit from the database
     * @param sessionID string that represents the current session and verifies the action
     * @param newOutfit ArrayList of ShopItems that will be for the updated outfit
     */
    @PostMapping("/updatePetOutfit")
    public ResponseEntity<Object> updatePetOutfit(@RequestParam SessionID sessionID, ArrayList<ShopItem> newOutfit){
        return this.petSystemFacade.updateCurrentOutfit(sessionID, newOutfit);
    }

    /**
     * Get request to get Pet Balance with the sessionID from the database
     * @param sessionID string that represents the current session and verifies the action
     */
    @GetMapping("/petBalance")
    public ResponseEntity<Object> getBalance(@RequestParam SessionID sessionID){
        return this.petSystemFacade.getBalance(sessionID);
    }


}
