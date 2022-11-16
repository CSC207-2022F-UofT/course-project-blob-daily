package com.backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountsRepo extends MongoRepository<com.backend.entities.users.DBAccount, String> {
    com.backend.entities.users.DBAccount findByAccountID(String accountID);
    com.backend.entities.users.DBAccount findBySessionID(String sessionID);
    com.backend.entities.users.DBAccount findByUsername(String username);
}
