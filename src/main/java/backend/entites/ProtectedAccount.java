package backend.entites;

import java.sql.Timestamp;

public class ProtectedAccount {
    // Instance Variables
    private final String username;
    private final Timestamp timestamp;

    // Constructor
    public ProtectedAccount(String username, Timestamp timestamp) {
        this.username = username;
        this.timestamp = timestamp;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
