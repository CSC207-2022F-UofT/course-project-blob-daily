package com.backend.controller;

import com.backend.entities.Friend;
import com.backend.entities.IDs.ID;
import com.backend.repositories.FriendsRepo;
import com.backend.usecases.FriendsManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class FriendController {
    public static FriendsRepo friendsRepo;

    public FriendController(FriendsRepo friendsRepo) {
        FriendController.friendsRepo = friendsRepo;
    }

    @GetMapping("/friends/getFriends")
    public ArrayList<String> getFriends(@RequestParam String userID) {
        return FriendsManager.getFriends(userID);
    }

//    @PostMapping("/friends/addFriend")
//    public ResponseEntity<Object> addFriend(@RequestParam String userName, @RequestParam String friendUserName, @RequestParam String sessionID) {
//        return FriendsManager.addFriend(userName, friendUserName, sessionID);
//    }
//
//    @DeleteMapping("/friends/deleteFriend")
//    public ResponseEntity<Object> deleteFriend(@RequestParam String userName, @RequestParam String friendUserName, @RequestParam String sessionID) {
//        return FriendsManager.deleteFriend(userName, friendUserName, sessionID);
//    }
}
