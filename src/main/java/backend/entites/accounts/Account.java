package backend.entites.accounts;

import backend.entites.IDs.AccountID;
import backend.entites.IDs.SessionID;

import java.sql.Timestamp;

public class Account extends ProtectedAccount{
    // Instance Variables
    private AccountID accountID;
    private SessionID sessionID;
    private final Password password;
    private Timestamp timestamp;

    // Constructors
    public Account(AccountID accountID, String username, String password, Timestamp timestamp) throws Exception {
        super(username, timestamp);
        this.accountID = accountID;
        this.password = new Password(password);
    }

    public Account(SessionID sessionID, String username, String password) throws Exception {
        super(username);
        this.sessionID = sessionID;
        this.password = new Password(password);
    }

    // Getters
    public AccountID getAccountID() {
        return accountID;
    }

    public SessionID getSessionID() {
        return sessionID;
    }

    public String getPassword() {
        return password.getPassword();
    }
}
