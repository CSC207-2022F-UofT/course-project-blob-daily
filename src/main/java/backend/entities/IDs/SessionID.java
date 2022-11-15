package backend.entities.IDs;

import backend.entities.criteria.Criteria;
import backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
import backend.entities.criteria.conditions.CriteriaExpression;
import backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import backend.entities.criteria.conditions.SizeExpression;

import java.util.ArrayList;
import java.util.List;

public class SessionID extends backend.entities.IDs.ID {
    public SessionID(String defaultID) {
        super(defaultID, new Criteria(new ArrayList<>(List.of(
                new SizeExpression(16, defaultID),
                new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter")), defaultID),
                new ContainsAtleastTypeExpression(new ArrayList<>(List.of("number", "letter", "uppercase", "lowercase")), defaultID)
        ))));
    }
}
