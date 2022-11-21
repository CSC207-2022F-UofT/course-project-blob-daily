package com.backend.controller;

import com.backend.entities.IDs.SessionID;
import com.backend.repositories.AccountsRepo;
import com.backend.repositories.FeedRepo;
import com.backend.usecases.AccountManager;
import com.backend.usecases.FeedManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FeedController {

    public static FeedRepo feedRepo;

    public FeedController(FeedRepo feedRepo) {
        FeedController.feedRepo = feedRepo;
    }

    @GetMapping("/feed" )
    public ResponseEntity<Object> getFeedItems(@RequestParam String sessionID){
        // Run FeedtManager service
        return FeedManager.getFeedItems(sessionID);
    }
}
