package com.backend.entities.users.info;

import com.backend.entities.criteria.Criteria;
import com.backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
import com.backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import com.backend.entities.criteria.conditions.SizeRangeExpression;
import com.backend.entities.criteria.generatable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Username Entity (username string, validity, generation, etc).
 */
public class Username implements generatable {
    // Instance Variables
    public static final Criteria criteria = new Criteria(new ArrayList<>(List.of(
            new SizeRangeExpression(5, 20, null),
            new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter")), null),
            new ContainsAtleastTypeExpression(new ArrayList<>(List.of("letter")), null)
    )));

    private String username;

    // Constructor
    public Username(String username) {
        this.username = username;

        if (username != null) {
            this.isValid(username, criteria);
        }
    }

    /**
     * Format the string representation for this Object class
     * @return the username string variable
     */
    @Override
    public String toString() {
        return this.username;
    }

    /**
     * Generate a new username (and set to current) with the implicitly defined criteria of a username
     */
    public void generateUsername() {
        this.username = this.generate(criteria);
    }

    /**
     * Generate and return a new username based on the implicitly defined criteria of a username
     * @return a new username based on the implicitly defined criteria of a username
     */
    @SuppressWarnings("unused")
    public String suggestUsername() {
        return this.generate(criteria);
    }

    /**
     * Check if the current username stored in this object is valid based on the implicitly defined criteria of a username
     * @return Whether the current username is a valid username
     */
    public boolean isValid() {
        return this.isValid(this.username, criteria);
    }
}
