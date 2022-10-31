package backend.entities.accounts;

import backend.entities.criteria.Criteria;
import backend.entities.criteria.CriteriaExpression;
import backend.entities.criteria.verifiable;

import java.util.ArrayList;
import java.util.List;

/**
 * @param username Instance Variable
 */
public record Username(String username) implements verifiable {
    // Constructor
    public Username(String username) {
        this.username = username;

        Criteria criteria = new Criteria(new ArrayList<CriteriaExpression>(List.of(
                new CriteriaExpression("5", "at least size", username),
                new CriteriaExpression("20", "at most size", username),
                new CriteriaExpression(new ArrayList<String>(List.of("number", "letter")), username),
                new CriteriaExpression("number", "contains type", username),
                new CriteriaExpression("letter", "contains type", username),
                new CriteriaExpression("uppercase", "contains type", username),
                new CriteriaExpression("lowercase", "contains type", username)
        )));

        this.isValid(username, criteria);
    }
}
