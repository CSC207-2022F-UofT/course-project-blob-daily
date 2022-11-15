package backend.entities.IDs;

import backend.entities.criteria.Criteria;
import backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
import backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import backend.entities.criteria.conditions.SizeExpression;

import java.util.ArrayList;
import java.util.List;

public class SessionID extends ID {
    public static Criteria criteria = new Criteria(new ArrayList<>(List.of(
            new SizeExpression(16, null),
            new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter")), null),
            new ContainsAtleastTypeExpression(new ArrayList<>(List.of("number", "letter", "uppercase", "lowercase")), null)
    )));

    public SessionID(String defaultID) {
        super(defaultID, criteria);
    }
}
