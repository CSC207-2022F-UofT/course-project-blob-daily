package com.backend.entities.users;

import com.backend.entities.users.info.Username;
import net.minidev.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

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
    public Username getUsername() {
        return this.username;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public JSONObject getJSONObject() {
        JSONObject entity = new JSONObject();

        entity.put("username", this.username.toString());
        if (this.timestamp != null) entity.put("timestamp", this.timestamp.toString());

        return entity;
    }
}
