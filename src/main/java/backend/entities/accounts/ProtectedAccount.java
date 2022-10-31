package backend.entities.accounts;

import java.sql.Timestamp;

public class ProtectedAccount {
    // Instance Variables
    private final Username username;
    private final Timestamp timestamp;

    // Constructors
    public ProtectedAccount(String username, Timestamp timestamp) {
        this.username = new Username(username);
        this.timestamp = timestamp;
    }

    public ProtectedAccount(String username) {
        this.username = new Username(username);
        this.timestamp = null;
    }

    // Getters
    public String getUsername() {
        return username.username();
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
