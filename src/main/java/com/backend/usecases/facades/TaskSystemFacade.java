package com.backend.usecases.facades;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.TaskCompletionRecord;
import com.backend.error.exceptions.SessionException;
import com.backend.usecases.IErrorHandler;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.BalanceManager;
import com.backend.usecases.managers.HealthManager;
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
    private final BalanceManager balanceManager;
    private final HealthManager healthManager;
    private final IErrorHandler errorHandler;

    @Autowired
    public TaskSystemFacade(TaskManager taskManager, AccountManager accountManager, BalanceManager balanceManager, HealthManager healthManager, IErrorHandler errorHandler) {
        this.taskManager = taskManager;
        this.accountManager = accountManager;
        this.balanceManager = balanceManager;
        this.healthManager = healthManager;
        this.errorHandler = errorHandler;
    }

    public ResponseEntity<?> completeTask(SessionID sessionID, String name, String image, double reward) {
        AccountID account = this.accountManager.verifySession(sessionID);
        //check if the sessionID is valid
        if (account == null) {
            return this.errorHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);
        }

        //check if the task name and reward are correct
        if (!this.taskManager.verifyTask(name, image, reward)) {
            return this.errorHandler.logError(new Exception("Task does not exist"), HttpStatus.BAD_REQUEST);
        }

        //check if the task has already been completed today
        if (this.taskManager.checkCompleted(name, account)){
            return this.errorHandler.logError(new Exception("Record already exists"), HttpStatus.BAD_REQUEST);
        }

        //update the pet
        this.balanceManager.updateBalance(account.getID(), reward);
        this.healthManager.updateHealth(account.getID(), reward / 5);
        TaskCompletionRecord record = this.taskManager.completeTask(account, name, image);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    public ResponseEntity<?> getActiveTasks(SessionID sessionID) {
        AccountID account = this.accountManager.verifySession(sessionID);
        if (account == null) {
            //check if the sessionID is valid
            return this.errorHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(this.taskManager.activeTasks(account), HttpStatus.OK);
    }
}