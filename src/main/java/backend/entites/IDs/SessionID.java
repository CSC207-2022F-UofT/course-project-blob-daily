package backend.entites.IDs;

import backend.entites.criteria.Criteria;
import backend.entites.criteria.CriteriaExpression;

import java.util.ArrayList;
import java.util.List;

public class SessionID extends backend.entites.IDs.ID {
    public SessionID(String defaultID) throws Exception {
        super(defaultID, new Criteria(new ArrayList<CriteriaExpression>(List.of(
                new CriteriaExpression("10", "at least size", defaultID),
                new CriteriaExpression("19", "at most size", defaultID),
                new CriteriaExpression(new ArrayList<String>(List.of("number", "letter")), defaultID),
                new CriteriaExpression("number", "contains type", defaultID),
                new CriteriaExpression("letter", "contains type", defaultID),
                new CriteriaExpression("uppercase", "contains type", defaultID),
                new CriteriaExpression("lowercase", "contains type", defaultID)
        ))));
    }
}
