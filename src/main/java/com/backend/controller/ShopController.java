package com.backend.controller;

import com.backend.entities.Pet;
import com.backend.entities.ShopItem;
import com.backend.repositories.ShopItemRepo;
import com.backend.usecases.ShopManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ShopController {

    public static ShopItemRepo shopRepo;

    public ShopController(ShopItemRepo shopRepo) {
        ShopController.shopRepo = shopRepo;
    }

    /**
     * Get request to getShopItem with the sessionID from the database
     *
     */
    @GetMapping("/shopItems")
    public ResponseEntity<Object> shopItems(){

        ArrayList<ShopItem> shopItems = ShopManager.getShopItems();

        return new ResponseEntity<>(shopItems, HttpStatus.OK);
    }



}