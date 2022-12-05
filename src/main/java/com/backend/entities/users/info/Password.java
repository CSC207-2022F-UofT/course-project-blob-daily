package com.backend.entities.users.info;

import com.backend.entities.criteria.Criteria;
import com.backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
import com.backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import com.backend.entities.criteria.conditions.SizeRangeExpression;
import com.backend.entities.criteria.Generatable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Password Entity (password string, validity, generation, etc).
 */
public class Password implements Generatable {
    // Instance Variables
    public static final Criteria criteria = new Criteria(new ArrayList<>(List.of(
            new SizeRangeExpression(5, 20, null),
            new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), null),
            new ContainsAtleastTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), null)
    )));
    private String password;

    // Constructor
    public Password(String password) {
        this.password = password;

        if (password != null) {
            this.isValid(password, criteria);
        }
    }

    /**
     * Format the string representation for this Object class
     * @return the password string variable
     */
    @Override
    public String toString() {
        return this.password;
    }

    /**
     * Generate a new password (and set to current) with the implicitly defined criteria of a password
     */
    public void generatePassword() {
        this.password = this.generate(criteria);
    }

    /**
     * Set the current password to the given parameter
     * @param newPassword of type String, newPassword to set the current password to
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Generate and return a new password based on the implicitly defined criteria of a password
     * @return a new password based on the implicitly defined criteria of a password
     */
    @SuppressWarnings("unused")
    public String suggestPassword() {
        return this.generate(criteria);
    }

    /**
     * Check if the current password stored in this object is valid based on the implicitly defined criteria of a password
     * @return Whether the current password is a valid password
     */
    public boolean isValid() {
        return this.isValid(this.password, criteria);
    }
}
