package com.backend.entities.IDs;

import com.backend.entities.criteria.Criteria;
import com.backend.entities.criteria.Generatable;

/**
 * Representation of an ID Entity (ID, validity, generation, etc)
 */
public class ID implements Generatable {
    // Instance Variables
    private String ID;
    private final Criteria criteria;

    // Constructors
    public ID(String defaultID, Criteria criteria) {
        this.ID = defaultID;
        this.criteria = criteria;

        if(this.ID != null) {
            this.isValid(this.ID, this.criteria);
        }
    }

    // ID Methods

    /**
     * Generate a new ID (and set to current) with the instance variable criteria
     */
    public void generateID() {
        this.ID = generate(this.criteria);
    }

    // Getter

    /**
     * Retrieve the current string representation of ID
     * @return the current string representation of the ID
     */
    public String getID() {
        return this.ID;
    }

    /**
     * Get the current criteria stored in this ID instance
     * @return the current criteria
     */
    public Criteria getCriteria() {
        return this.criteria;
    }

    /**
     * Check if the current ID is valid based on the current criteria
     * @return Whether the current ID is valid
     */
    public boolean isValid() {
        return this.isValid(this.ID, this.criteria);
    }

    /**
     * Retrieve the current string representation of ID
     * @return the current string representation of the ID
     */
    @Override
    public String toString() {
        return this.ID;
    }

    /**
     * Set the current ID to the given parameter (newID)
     * @param newID of type String, the current ID is set to this newID
     */
    public void setID(String newID) {
        this.ID = newID;
    }
}
