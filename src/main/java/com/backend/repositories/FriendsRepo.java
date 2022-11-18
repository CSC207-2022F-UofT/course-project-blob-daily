package com.backend.repositories;

import com.backend.entities.Friend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;

public interface FriendsRepo extends MongoRepository<Friend, String> {

    @Query("{ 'ID' : ?0 }")
    ArrayList<String> getFriendsList(String userID);
}
