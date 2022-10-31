package backend.entities.accounts;

import backend.entities.criteria.Criteria;
import backend.entities.criteria.CriteriaExpression;
import backend.entities.criteria.verifiable;

import java.util.ArrayList;
import java.util.List;

/**
 * @param password Instance Variable
 */
public record Password(String password) implements verifiable {
    // Constructor
    public Password(String password) {
        this.password = password;

        Criteria criteria = new Criteria(new ArrayList<CriteriaExpression>(List.of(
                new CriteriaExpression("5", "at least size", password),
                new CriteriaExpression("20", "at most size", password),
                new CriteriaExpression(new ArrayList<String>(List.of("number", "letter", "special")), password),
                new CriteriaExpression("number", "contains type", password),
                new CriteriaExpression("letter", "contains type", password),
                new CriteriaExpression("special", "contains type", password)
        )));

        this.isValid(password, criteria);
    }
}
