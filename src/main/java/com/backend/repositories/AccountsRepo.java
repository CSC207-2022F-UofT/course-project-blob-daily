package com.backend.repositories;

import com.backend.entities.users.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AccountsRepo extends MongoRepository<com.backend.entities.users.Account, String> {
    @Query("{accountID :?0}")
    Account findByAccountID(String accountID);
    @Query("{sessionID :?0}")
    Account findBySessionID(String sessionID);
    @Query("{sessionID :?0}")
    Account findAccountID(String sessionID);
    @Query("{username :?0, password : ?1}")
    Account findByCredentials(String username, String password);
}
