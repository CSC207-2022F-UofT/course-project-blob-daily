package backend.entities.accounts;

import backend.entities.IDs.AccountID;
import backend.entities.IDs.SessionID;

import java.sql.Timestamp;

public class Account extends ProtectedAccount{
    // Instance Variables
    private AccountID accountID;
    private SessionID sessionID;
    private final Password password;
    private Timestamp timestamp;

    // Constructors
    public Account(AccountID accountID, String username, String password, Timestamp timestamp) {
        super(username, timestamp);
        this.accountID = accountID;
        this.password = new Password(password);
    }

    public Account(SessionID sessionID, String username, String password) {
        super(username);
        this.sessionID = sessionID;
        this.password = new Password(password);
    }

    // Getters
    public AccountID getAccountID() {
        return this.accountID;
    }

    public SessionID getSessionID() {
        return this.sessionID;
    }

    public String getPassword() {
        return this.password.password();
    }

    @Override
    public Timestamp getTimestamp() {
        return this.timestamp;
    }
}
