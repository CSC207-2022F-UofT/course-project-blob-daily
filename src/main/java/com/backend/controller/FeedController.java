package com.backend.controller;

import com.backend.usecases.facades.FeedSystemFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedController {
    private final FeedSystemFacade feedSystemFacade;

    /**
     * Spring Boot Dependency Injection of the accountSystemFacade
     * @param feedSystemFacade the dependency to be injected
     */
    @Autowired
    public FeedController(FeedSystemFacade feedSystemFacade) {
        this.feedSystemFacade = feedSystemFacade;
    }

    /**
     * Get a list of feed data representing information regarding recent posts made by friends
     * @param sessionID of type String, password to reference associated account
     * @return a response entity detailing successful completion (with a newly generated SessionID) or any associated error
     */
    @GetMapping("/feed" )
    public ResponseEntity<Object> getFeed(@RequestParam String sessionID){
        // Run AccountManager service
        return this.feedSystemFacade.getFeed(sessionID);
    }
}
