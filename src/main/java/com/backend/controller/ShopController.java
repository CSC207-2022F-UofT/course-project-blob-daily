package com.backend.controller;

import com.backend.usecases.facades.PetSystemFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopController {

    private final PetSystemFacade petSystemFacade;

    @Autowired
    @SuppressWarnings("unused")
    public ShopController(PetSystemFacade petSystemFacade){
        this.petSystemFacade = petSystemFacade;
    }


    /**
     * Get request to getShopItem with the sessionID from the database
     */
    @GetMapping("/shopItems")
    public ResponseEntity<Object> getShopItems(){
        return petSystemFacade.getShopItems();
    }

    /**
     * Post request to have the pet own the item and reduce the balance accordingly to the cost of the item from the database
     * @param sessionID string that represents the current session and verifies the action
     * @param itemId string that represents the item to be purchased
     */
    @PostMapping("/purchaseItems")
    public ResponseEntity<Object> purchaseItem(@RequestParam String sessionID, String itemId){
        return petSystemFacade.purchaseItem(sessionID, itemId);
    }
}