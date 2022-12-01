package com.backend.usecases.facades;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.TaskCompletionRecord;
import com.backend.error.exceptions.SessionException;
import com.backend.error.handlers.LogHandler;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.ShopManager;
import com.backend.usecases.managers.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Task related facade
 */
@Service
@Configurable
public class TaskSystemFacade {
    private final TaskManager taskManager;
    private final AccountManager accountManager;
    private final ShopManager shopManager;

    @Autowired
    public TaskSystemFacade(TaskManager taskManager, AccountManager accountManager, ShopManager shopManager) {
        this.taskManager = taskManager;
        this.accountManager = accountManager;
        this.shopManager = shopManager;
    }

    public ResponseEntity<?> completeTask(SessionID sessionID, String name, String image, double reward) {
        AccountID account = this.accountManager.verifySession(sessionID);
        //check if the sessionID is valid
        if (account == null) {
            return LogHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);
        }

        //check if the task name and reward are correct
        if (this.taskManager.verifyTask(name, reward)) {
            return LogHandler.logError(new Exception("Task does not exist"), HttpStatus.BAD_REQUEST);
        }

        //check if the task has already been completed today
        if (this.taskManager.checkCompleted(name, account)){
            return LogHandler.logError(new Exception("Record already exists"), HttpStatus.BAD_REQUEST);
        }

        //update the pet
//        this.shopManager.updateBalance(sessionID, reward);
//        this.shopManager.updateHealth(sessionID);
        TaskCompletionRecord record = this.taskManager.completeTask(account, name, image);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    public ResponseEntity<?> getActiveTasks(SessionID sessionID) {
        AccountID account = AccountManager.verifySession(sessionID);
        if (account == null) {
            //check if the sessionID is valid
            return LogHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(this.taskManager.activeTasks(account), HttpStatus.OK);
    }
}
