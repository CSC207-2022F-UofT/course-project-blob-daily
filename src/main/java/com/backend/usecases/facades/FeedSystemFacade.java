package com.backend.usecases.facades;

import com.backend.entities.FeedItem;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.TaskCompletionRecord;
import com.backend.error.exceptions.SessionException;
import com.backend.usecases.IErrorHandler;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.PetManager;
import com.backend.usecases.managers.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FeedSystemFacade {
    private final AccountManager accountManager;
    private final TaskManager taskManager;
    private final PetManager petManager;
    private final IErrorHandler errorHandler;

    /**
     * Spring Boot Dependency Injection
     * @param accountManager the dependency to be injected
     * @param taskManager the dependency to be injected
     * @param petManager the dependency to be injected
     * @param errorHandler the dependency to be injected
     */
    @Autowired
    public FeedSystemFacade(AccountManager accountManager, TaskManager taskManager, PetManager petManager, IErrorHandler errorHandler) {
        this.accountManager = accountManager;
        this.taskManager = taskManager;
        this.petManager = petManager;
        this.errorHandler = errorHandler;
    }

    /**
     * Get a list of feed data representing information regarding recent posts made by friends
     * @param sessionID of type String, password to reference associated account
     * @return a response entity detailing successful completion (with a newly generated SessionID) or any associated error
     */
    public ResponseEntity<Object> getFeed(String sessionID) {
        // verify session
        AccountID accountID = this.accountManager.verifySession(new SessionID(sessionID));
        if (accountID == null) {
            return this.errorHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);
        }

        // get list of friends
        List<String> friends = new ArrayList<>(List.of("7T~Sp2w%Vl9t\"Wk4V]Fp"));

        // wrap FeedItem Objects
        List<FeedItem> feedItems = new ArrayList<>();

        for (TaskCompletionRecord record : this.taskManager.getAllTaskCompletionRecords()) {
            if (friends.contains(record.getAccountID())) {
                feedItems.add(new FeedItem(this.accountManager.getAccountInfo(record.getAccountIDObject()), record, this.petManager.getPet(record.getAccountID())));
            }
        }
        Collections.reverse(feedItems);

        return new ResponseEntity<>(feedItems, HttpStatus.OK);
    }
}
