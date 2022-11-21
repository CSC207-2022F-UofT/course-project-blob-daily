package com.backend.repositories;

import com.backend.entities.Friend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface FriendsRepo extends MongoRepository<Friend, String> {

    @Query("{ 'userID' : ?0 }")
    Friend findByUserID(String userID);


    @Query("{'friends':  {'$in': [?0]}}")
    List<Friend> findAllContainingUserID(String userID);
}
