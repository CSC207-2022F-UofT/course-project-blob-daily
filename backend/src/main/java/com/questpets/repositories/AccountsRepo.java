package com.questpets.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountsRepo extends MongoRepository<com.questpets.entities.users.Account, String> {
    com.questpets.entities.users.Account findByAccountID(com.questpets.entities.IDs.AccountID accountID);
    com.questpets.entities.users.Account findBySessionID(com.questpets.entities.IDs.SessionID sessionID);
    com.questpets.entities.users.Account findByUsername(String username);
}
