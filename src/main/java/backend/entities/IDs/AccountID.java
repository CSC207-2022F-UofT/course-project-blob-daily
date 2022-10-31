package backend.entities.IDs;

import backend.entities.criteria.Criteria;
import backend.entities.criteria.CriteriaExpression;

import java.util.ArrayList;
import java.util.List;

public class AccountID extends backend.entities.IDs.ID {
    public AccountID(String defaultID) {
        super(defaultID, new Criteria(new ArrayList<CriteriaExpression>(List.of(
                new CriteriaExpression("20", "size", defaultID),
                new CriteriaExpression(new ArrayList<String>(List.of("number", "letter", "special")), defaultID),
                new CriteriaExpression("number", "contains type", defaultID),
                new CriteriaExpression("letter", "contains type", defaultID),
                new CriteriaExpression("special", "contains type", defaultID),
                new CriteriaExpression("uppercase", "contains type", defaultID),
                new CriteriaExpression("lowercase", "contains type", defaultID)
        ))));
    }
}
