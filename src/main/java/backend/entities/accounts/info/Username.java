package backend.entities.accounts.info;

import backend.entities.criteria.Criteria;
import backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
import backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import backend.entities.criteria.conditions.SizeRangeExpression;
import backend.entities.criteria.generatable;

import java.util.ArrayList;
import java.util.List;

public class Username implements generatable {
    // Instance Variables
    private final Criteria criteria;
    private String username;

    // Constructor
    public Username(String username) {
        this.username = username;

        Criteria criteria = new Criteria(new ArrayList<>(List.of(
                new SizeRangeExpression(5, 20, username),
                new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter")), username),
                new ContainsAtleastTypeExpression(new ArrayList<>(List.of("number", "letter", "uppercase", "lowercase")), username)
        )));

        this.criteria = criteria;

        if (username != null) {
            this.isValid(username, criteria);
        }
    }

    @Override
    public String toString() {
        return this.username;
    }

    public void generateUsername() {
        this.username = this.generate(this.criteria);
    }

    public String suggestUsername() {
        return this.generate(criteria);
    }
}
