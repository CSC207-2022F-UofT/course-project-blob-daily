package com.backend.controller;

import com.backend.repositories.FriendsRepo;
import com.backend.usecases.FriendsManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

public class FriendController {
    public static FriendsRepo friendsRepo;

    public FriendController(FriendsRepo friendsRepo) {
        FriendController.friendsRepo = friendsRepo;
    }

    @GetMapping("/friends/getFriends")
    public ArrayList<String> getFriends(String userID) {
        return FriendsManager.getFriends(userID);
    }

    @PostMapping("/friends/addFriend")
    public ResponseEntity<Object> addFriend(String userName, String friendUsername) {
        return FriendsManager.addFriend(userName, friendUsername);
    }
}
