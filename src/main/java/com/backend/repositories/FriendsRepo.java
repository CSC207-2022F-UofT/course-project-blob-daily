package com.backend.repositories;

import com.backend.entities.Friend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

/**
 * Mongo Repository for custom queries on the Friends Collection
 */
public interface FriendsRepo extends MongoRepository<Friend, String> {
    /**
     * Find all associated Friends instance containing userID in friends list
     * @param userID of type String, accountID to reference the desired Account
     * @return the list of accounts that have the desired accountID in their friends list
     */
    @Query("{'friends':  {'$in': [?0]}}")
    List<Friend> findAllContainingUserID(String userID);
}
