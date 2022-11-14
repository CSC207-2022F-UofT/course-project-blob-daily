package backend.entities.IDs;

import backend.entities.criteria.Criteria;
import backend.entities.criteria.generatable;
import backend.error.handlers.LogHandler;

public class ID implements generatable {
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
    public void generateID() {
        this.ID = generate(this.criteria);
    }

    // Getter
    public String getID() {
        return this.ID;
    }

    public Criteria getCriteria() {
        return this.criteria;
    }

    @Override
    public String toString() {
        return this.ID;
    }
}
