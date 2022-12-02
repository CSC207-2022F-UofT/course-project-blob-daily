package com.backend.usecases.facades;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.ShopItem;
import com.backend.entities.TaskCompletionRecord;
import com.backend.error.exceptions.SessionException;
import com.backend.usecases.IErrorHandler;
import com.backend.usecases.managers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Configurable
public class PetSystemFacade {

    private final ShopManager shopManager;
    private final AccountManager accountManager;
    private final PetManager petManager;
    private final HealthManager healthManager;
    private final BalanceManager balanceManager;
    private final IErrorHandler errorHandler;
    private final TaskManager taskManager;

    @Autowired
    public PetSystemFacade(ShopManager shopManager, AccountManager accountManager,
                           PetManager petManager, HealthManager healthManager, BalanceManager balanceManager, IErrorHandler errorHandler, TaskManager taskManager){
        this.shopManager = shopManager;
        this.accountManager = accountManager;
        this.petManager = petManager;
        this.healthManager = healthManager;
        this.balanceManager = balanceManager;
        this.errorHandler = errorHandler;
        this.taskManager = taskManager;
    }

    /**
     * Post request of the pet owning the item and subtracting from its balance the cost of the item
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

    public ResponseEntity<Object> updateCurrentOutfit(SessionID sessionID, ArrayList<ShopItem> newOutfit){
        AccountID accountID = this.accountManager.verifySession(sessionID);
        if (accountID == null){
            return this.errorHandler.logError(new SessionException("Account ID is null since sessionID was invalid"), HttpStatus.BAD_REQUEST);
        }
        boolean result = this.shopManager.updateCurrentOutfit(accountID, newOutfit);
        if (!result){
            return this.errorHandler.logError(new SessionException("Account ID is null since sessionID was invalid"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Outfit successfully updated", HttpStatus.OK);
    }


    public ResponseEntity<Object> getPet(SessionID sessionID){
        AccountID accountID = this.accountManager.verifySession(sessionID);
        if (accountID == null){
            return this.errorHandler.logError(new SessionException("Account ID is null since sessionID was invalid"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(petManager.getPet(accountID.getID()), HttpStatus.OK);
    }

    public ResponseEntity<Object> addHealth(SessionID sessionID, double amount){
        AccountID accountID = this.accountManager.verifySession(sessionID);
        if (accountID == null){
            return this.errorHandler.logError(new SessionException("Account ID is null since sessionID was invalid"), HttpStatus.BAD_REQUEST);
        }
        this.healthManager.updateHealth(accountID.getID(), amount);
        return new ResponseEntity<>("Health successfully updated", HttpStatus.OK);
    }

    public ResponseEntity<Object> healthDecay(SessionID sessionID){
        AccountID accountID = this.accountManager.verifySession(sessionID);
        if (accountID == null){
            return this.errorHandler.logError(new SessionException("Account ID is null since sessionID was invalid"), HttpStatus.BAD_REQUEST);
        }
        List<TaskCompletionRecord> completionRecords = this.taskManager.getTaskCompletionRecords(accountID);
        this.healthManager.healthDecay(accountID.getID(), completionRecords);
        return new ResponseEntity<>("Health Decay checked", HttpStatus.OK);
    }

    public ResponseEntity<Object> getBalance(SessionID sessionID){
        AccountID accountID = this.accountManager.verifySession(sessionID);
        if (accountID == null){
            return this.errorHandler.logError(new SessionException("Account ID is null since sessionID was invalid"), HttpStatus.BAD_REQUEST);
        }

        double balance = this.balanceManager.getBalance(accountID.getID());
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateBalance(SessionID sessionID, double amount){
        AccountID accountID = this.accountManager.verifySession(sessionID);
        if (accountID == null){
            return this.errorHandler.logError(new SessionException("Account ID is null since sessionID was invalid"), HttpStatus.BAD_REQUEST);
        }

        this.balanceManager.updateBalance(sessionID.getID(), amount);
        return new ResponseEntity<>("Balance successfully updated", HttpStatus.OK);
    }

    public ResponseEntity<Object> getShopItems(){
        return new ResponseEntity<>(this.shopManager.getShopItems(), HttpStatus.OK);
    }
}
