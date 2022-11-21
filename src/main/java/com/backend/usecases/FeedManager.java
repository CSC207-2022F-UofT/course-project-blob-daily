package com.backend.usecases;

import com.backend.controller.AccountController;
import com.backend.controller.FeedController;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.ID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.users.Account;
import com.backend.entities.users.ProtectedAccount;
import com.backend.entities.users.info.Password;
import com.backend.entities.users.info.Username;
import com.backend.error.exceptions.AccountInfoException;
import com.backend.error.exceptions.IDException;
import com.backend.error.exceptions.SessionException;
import com.backend.error.handlers.LogHandler;
import com.backend.repositories.FeedRepo;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

@Service
@Configurable
public class FeedManager {

    public static ResponseEntity<Object> getFeedItems(String sessionID) {
        // Make DB call to find account based on id
        Account account = AccountController.accountsRepo.findAccountID(sessionID);

        // Check if found
        if (account == null) return new ResponseEntity<>("No account found with the given sessionID", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<Object>(FeedController.feedRepo.findByAccountID(account.getAccountID()), HttpStatus.OK);
    }

}
