package com.backend.controller;


import com.backend.usecases.facades.FriendSystemFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FriendController {

    private final FriendSystemFacade friendSystemFacade;

    @Autowired
    public FriendController(FriendSystemFacade friendSystemFacade) {
        this.friendSystemFacade = friendSystemFacade;
    }

    @GetMapping("/friends/getFriends")
    public ResponseEntity<Object> getFriends(@RequestParam String sessionID) {
        return this.friendSystemFacade.getUserFriends(sessionID);
    }

    @DeleteMapping("/friends/deleteFriend")
    public ResponseEntity<Object> deleteFriend(@RequestParam String friendUserName, @RequestParam String sessionID) {
        return this.friendSystemFacade.deleteFriend(friendUserName, sessionID);
    }

    @DeleteMapping("/friends/deleteAllCorrelatedFriends")
    public ResponseEntity<Object> deleteALlCorrelatedFriends(@RequestParam String sessionID) {
        return this.friendSystemFacade.deleteAllCorrelatedFriends(sessionID);
    }
}
