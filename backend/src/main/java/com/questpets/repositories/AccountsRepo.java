package com.questpets.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountsRepo extends MongoRepository<com.questpets.entities.users.DBAccount, String> {
    com.questpets.entities.users.DBAccount findByAccountID(String accountID);
    com.questpets.entities.users.DBAccount findBySessionID(String sessionID);
    com.questpets.entities.users.DBAccount findByUsername(String username);
}
