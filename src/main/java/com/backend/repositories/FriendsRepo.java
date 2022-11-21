package com.backend.repositories;

import com.backend.entities.Friend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface FriendsRepo extends MongoRepository<Friend, String> {
    @Query("{'friends':  {'$in': [?0]}}")
    List<Friend> findAllContainingUserID(String userID);
}
