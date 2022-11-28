package com.backend.repositories;

import com.backend.entities.users.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Mongo Repository for custom queries on the Account Collection
 */
public interface AccountsRepo extends MongoRepository<com.backend.entities.users.Account, String> {
    /**
     * Find the associated Account with the given parameter (by AccountID)
     * @param accountID of type String, accountID to reference the desired Account
     * @return the retrieved Account object given valid parameters and exists (otherwise null)
     */
    @Query("{accountID :?0}")
    Account findByAccountID(String accountID);

    /**
     * Find the associated Account with the given parameter (by SessionID)
     * @param sessionID of type String, sessionID to reference the desired Account
     * @return the retrieved Account object given valid parameters and exists (otherwise null)
     */
    @Query("{sessionID :?0}")
    Account findBySessionID(String sessionID);

    /**
     * Find the associated Account with the given parameter (by username and password)
     * @param username of type String, username to reference the desired Account
     * @param password of type String, password to validate the desired Account
     * @return the retrieved Account object given valid parameters and exists (otherwise null)
     */
    @Query("{username :?0, password : ?1}")
    Account findByCredentials(String username, String password);

    /**
     * Find the associated Account with the given parameter (by username)
     * @param username of type String, username to reference the desired Account
     * @return the retrieved Account object given valid parameters and exists (otherwise null)
     */
    @Query("{username :?0}")
    Account findByUsername(String username);
}
