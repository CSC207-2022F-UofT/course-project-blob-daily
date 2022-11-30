package com.backend.controller;


import com.backend.repositories.FriendsRepo;
import com.backend.usecases.FriendsManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FriendController {

    @GetMapping("/friends/getFriends")
    public ResponseEntity<Object> getFriends(@RequestParam String sessionID) {
        return FriendsManager.getUserFriends(sessionID);
    }

    @DeleteMapping("/friends/deleteFriend")
    public ResponseEntity<Object> deleteFriend(@RequestParam String friendUserName, @RequestParam String sessionID) {
        return FriendsManager.deleteFriend(friendUserName, sessionID);
    }
}
