package backend.entites.accounts;

import java.sql.Timestamp;

public class ProtectedAccount {
    // Instance Variables
    private final Username username;
    private final Timestamp timestamp;

    // Constructor
    public ProtectedAccount(String username, Timestamp timestamp) throws Exception {
        this.username = new Username(username);
        this.timestamp = timestamp;
    }

    public ProtectedAccount(String username) throws Exception {
        this.username = new Username(username);
        this.timestamp = null;
    }

    // Getters
    public String getUsername() {
        return username.getUsername();
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
