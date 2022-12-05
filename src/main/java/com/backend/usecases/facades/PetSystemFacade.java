package com.backend.usecases.facades;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.ShopItem;
import com.backend.error.exceptions.SessionException;
import com.backend.usecases.IErrorHandler;
import com.backend.usecases.managers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@Configurable
public class PetSystemFacade {

    private final ShopManager shopManager;
    private final AccountManager accountManager;
    private final PetManager petManager;
    private final BalanceManager balanceManager;
    private final IErrorHandler errorHandler;

    /**
     * Spring Boot Dependency Injection of the accountManager
     * @param shopManager the dependency to be injected
     * @param accountManager the dependency to be injected
     * @param petManager the dependency to be injected
     * @param balanceManager the dependency to be injected
     * @param errorHandler the dependency to be injected
     */
    @Autowired
    @SuppressWarnings("unused")
    public PetSystemFacade(ShopManager shopManager, AccountManager accountManager, PetManager petManager, BalanceManager balanceManager, IErrorHandler errorHandler){
        this.shopManager = shopManager;
        this.accountManager = accountManager;
        this.petManager = petManager;
        this.balanceManager = balanceManager;
        this.errorHandler = errorHandler;
    }

    /**
     * Post request of the allowing the pet owning the item and subtracting from its balance the cost of the item if it fulfills the requirements
     * @param sessionID string that represents the current session and verifies the action
     * @param itemID string that represents which item the pet is attempting to access
     */
    public ResponseEntity<Object> purchaseItem(String sessionID, String itemID) {
        AccountID accountID = this.accountManager.verifySession(new SessionID(sessionID));
        if (accountID == null){
            return this.errorHandler.logError(new SessionException("Account ID is null since sessionID was invalid"), HttpStatus.BAD_REQUEST);
        }

        double difference = this.shopManager.checkSufficientBalance(accountID, itemID);

        ShopItem shopItem = this.shopManager.getShopItem(itemID);

        boolean owned = petManager.checkInventory(accountID, shopItem);
        if (owned){
            return new ResponseEntity<>("Item already owned", HttpStatus.OK);
        }

        if (difference > 0) {
            boolean result = this.shopManager.updateInventory(accountID.getID(), itemID);


            if (!result) {
                return this.errorHandler.logError(new SessionException("Item is null since itemID was invalid"), HttpStatus.BAD_REQUEST);
            }

            this.balanceManager.updateBalance(accountID.getID(), - shopItem.getCost());

            return new ResponseEntity<>("Item purchased", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.OK);
        }
    }

    /**
     * Post request of the allowing the pet to update the outfit
     * @param sessionID string that represents the current session and verifies the action
     * @param itemID ItemID of shopItem of outfit that the pet will wear
     */
    public ResponseEntity<Object> updateCurrentOutfit(SessionID sessionID, String itemID){
        AccountID accountID = this.accountManager.verifySession(sessionID);
        if (accountID == null){
            return this.errorHandler.logError(new SessionException("Account ID is null since sessionID was invalid"), HttpStatus.BAD_REQUEST);
        }
        ShopItem shopItem = this.shopManager.getShopItem(itemID);
        boolean result = this.shopManager.updateCurrentOutfit(accountID, shopItem);
        if (!result){
            return this.errorHandler.logError(new SessionException("Account ID is null since sessionID was invalid"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Outfit successfully updated", HttpStatus.OK);
    }


    /**
     * Get request of the pet by the sessionID from the database
     * @param sessionID string that represents the current session and verifies the action
     */
    public ResponseEntity<Object> getPet(SessionID sessionID){
        AccountID accountID = this.accountManager.verifySession(sessionID);
        if (accountID == null){
            return this.errorHandler.logError(new SessionException("Account ID is null since sessionID was invalid"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(petManager.getPet(accountID.getID()), HttpStatus.OK);
    }

    /**
     * Get request of all the shopItems from the database
     */
    public ResponseEntity<Object> getShopItems(){
        return new ResponseEntity<>(this.shopManager.getShopItems(), HttpStatus.OK);
    }
}
