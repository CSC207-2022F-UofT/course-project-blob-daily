package com.backend.usecases;

import com.backend.controller.AccountController;
import com.backend.controller.FeedController;
import com.backend.controller.TaskCompletionController;
import com.backend.entities.Friend;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.ID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Task;
import com.backend.entities.TaskCompletionRecord;
import com.backend.entities.users.Account;
import com.backend.entities.users.ProtectedAccount;
import com.backend.entities.users.info.Password;
import com.backend.entities.users.info.Username;
import com.backend.error.exceptions.AccountInfoException;
import com.backend.error.exceptions.IDException;
import com.backend.error.exceptions.SessionException;
import com.backend.error.handlers.LogHandler;
import com.backend.repositories.FeedRepo;
import com.backend.repositories.TaskCompletionRepo;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Configurable
public class FeedManager {

    public static ResponseEntity<Object> getFeedItems(String sessionID) {
        // Make DB call to find account based on id
        AccountID account = AccountManager.verifySession(new SessionID(sessionID));

        // Creates a list of user's friends
        ArrayList<String> friends = FriendsManager.getFriends(account.getID());

        // Creates a list of friend's completed tasks
        List<TaskCompletionRecord> completeTasks = TaskManager.getRecord(account);

        ArrayList allRecentTasks = new ArrayList<>();
        for (String userID : friends) {
            List<TaskCompletionRecord> singleUserCompletedTasks = TaskManager.getRecord(account);

            // get the most recent record (pre-condition: singleUserCompletedTasks is sorted by old to new)
            TaskCompletionRecord mostRecentRecord = null;
            if (singleUserCompletedTasks != null && !singleUserCompletedTasks.isEmpty()) {
                mostRecentRecord = singleUserCompletedTasks.get(singleUserCompletedTasks.size() - 1);
            }

            // Create new list of sorted tasks
            allRecentTasks.add(mostRecentRecord);


            // Check if found
            if (account == null)
                return new ResponseEntity<>("No account found with the given sessionID", HttpStatus.BAD_REQUEST);


        }
        return new ResponseEntity<>(allRecentTasks, HttpStatus.OK);
    }
}