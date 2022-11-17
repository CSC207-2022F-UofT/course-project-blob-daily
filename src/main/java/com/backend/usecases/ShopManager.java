package com.backend.usecases;

import com.backend.controller.PetController;
import com.backend.controller.ShopController;
import com.backend.entities.IDs.ItemID;
import com.backend.entities.Pet;
import com.backend.entities.ShopItem;
import com.backend.error.handlers.LogHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShopManager {

    public static void addShopItem(){

        ItemID ID = new ItemID(null);
        ID.generateID();
        ShopItem shopItem = new ShopItem(ID.getID(), 29.90, "brown boots", "a pair of boots that protects you from the snow");
        ShopController.shopRepo.save(shopItem);

    }

    public static void addPet(){
        ArrayList<ShopItem> curInventory = new ArrayList<>();
        curInventory.add(new ShopItem("124", 59.90, "hoodie", "provides warmth"));
        Pet pet = new Pet("9j(Gc5g)Id0G#Pt9s?De", 85.00, 0.0, curInventory, new ArrayList<>());
        PetController.petRepo.save(pet);
    }

    public static ArrayList<ShopItem> getShopItems() {
        return (ArrayList<ShopItem>) ShopController.shopRepo.findAll();
    }

    public static Optional<Pet> getPet(String sessionID){
        String curAccount = "9j(Gc5g)Id0G#Pt9s?De";
        Optional<Pet> pet = PetController.petRepo.findById(curAccount);
//        if (pet == null){
//            LogHandler.logError(new Exception("pet is null"));
//        }
        return pet;
    }

    public static boolean updateCurrentOutfit(String sessionID, List<ShopItem> newOutfit){
        String curAccount = "9j(Gc5g)Id0G#Pt9s?De";
        Optional<Pet> pet = PetController.petRepo.findById(curAccount);
//        if (pet == null){
//            LogHandler.logError(new Exception("pet is null"));
//        }
        Pet updatedPet = new Pet(curAccount, pet.get().getHealth(), 0.0, pet.get().getInventory(), newOutfit);
        PetController.petRepo.save(updatedPet);
        return true;
    }

    public static double getBalance(String sessionID){
        String curAccount = "9j(Gc5g)Id0G#Pt9s?De";
        Optional<Pet> pet = PetController.petRepo.findById(curAccount);
//        if (pet == null){
//            LogHandler.logError(new Exception("pet is null"));
//        }

        return pet.get().getBalance();
    }

    public static boolean purchaseItem(String itemID, String sessionID) {
        String curAccount = "9j(Gc5g)Id0G#Pt9s?De";
        Optional<Pet> pet = PetController.petRepo.findById(curAccount);
//        if (pet == null){
//            LogHandler.logError(new Exception("pet is null"));
//        }
        Optional<ShopItem> item = ShopController.shopRepo.findById(itemID);
//        if (item == null){
//            LogHandler.logError(new Exception("item is null"));
//        }
        return true;
    }
}
