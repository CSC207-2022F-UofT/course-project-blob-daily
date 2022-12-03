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

    @Autowired
    public FeedSystemFacade(AccountManager accountManager, TaskManager taskManager, PetManager petManager, IErrorHandler errorHandler) {
        this.accountManager = accountManager;
        this.taskManager = taskManager;
        this.petManager = petManager;
        this.errorHandler = errorHandler;
    }

    public ResponseEntity<Object> getFeed(String sessionID) {
        // verify session
        AccountID accountID = this.accountManager.verifySession(new SessionID(sessionID));
        if (accountID == null) {
            return this.errorHandler.logError(new SessionException("Invalid Session"), HttpStatus.BAD_REQUEST);
        }

        // get list of friends
        List<String> friends = new ArrayList<>(List.of("3q'Xc3z!If9W`Yf3G%Ms", "9j:Op4y<Ga6R+Cn9R>Lc", "1Q.Wm4V`Dm2Q$Fz8m]Ec"));

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
