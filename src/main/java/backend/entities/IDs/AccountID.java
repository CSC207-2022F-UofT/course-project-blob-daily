package backend.entities.IDs;

import backend.entities.criteria.Criteria;
import backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
import backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import backend.entities.criteria.conditions.SizeExpression;

import java.util.ArrayList;
import java.util.List;

public class AccountID extends ID {

    public static Criteria criteria = new Criteria(new ArrayList<>(List.of(
            new SizeExpression(20, null),
            new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), null),
            new ContainsAtleastTypeExpression(new ArrayList<>(List.of("special", "number", "letter", "uppercase", "lowercase")), null)
    )));

    public AccountID(String defaultID) {
        super(defaultID, criteria);
    }
}
