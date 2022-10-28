package backend.entites.IDs;

import backend.entites.criteria.Criteria;
import backend.entites.criteria.verifiable;

public class ID implements verifiable {
    // Instance Variables
    private final String ID;
    private final Criteria criteria;

    // Constructors
    public ID(String defaultID, Criteria criteria) throws Exception {
        this.ID = defaultID;
        this.criteria = criteria;

        this.isValid(this.ID, this.criteria);
    }

    // ID Methods
    public void generateID() {
    }

    // Getter
    public String getID() {
        return ID;
    }
}
