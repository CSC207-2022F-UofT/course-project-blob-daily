package com.backend.controller;


import com.backend.repositories.FriendsRepo;
import com.backend.usecases.FriendsManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FriendController {
    public static FriendsRepo friendsRepo;

    public FriendController(FriendsRepo friendsRepo) {
        FriendController.friendsRepo = friendsRepo;
    }

    @GetMapping("/friends/getFriends")
    public ResponseEntity<Object> getFriends(@RequestParam String userName, @RequestParam String sessionID) {
        return FriendsManager.getFriends(userName, sessionID);
    }

    @DeleteMapping("/friends/deleteFriend")
    public ResponseEntity<Object> deleteFriend(@RequestParam String userName, @RequestParam String friendUserName, @RequestParam String sessionID) {
        return FriendsManager.deleteFriend(userName, friendUserName, sessionID);
    }
}
