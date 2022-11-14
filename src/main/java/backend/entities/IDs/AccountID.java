package backend.entities.IDs;

import backend.entities.accounts.info.Password;
import backend.entities.accounts.info.Username;
import backend.entities.criteria.Criteria;
import backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
import backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import backend.entities.criteria.conditions.CriteriaExpression;
import backend.entities.criteria.conditions.SizeExpression;

import java.util.ArrayList;
import java.util.List;

public class AccountID extends backend.entities.IDs.ID {
    public AccountID(String defaultID) {
        super(defaultID, new Criteria(new ArrayList<>(List.of(
                new SizeExpression(20, defaultID),
                new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), defaultID),
                new ContainsAtleastTypeExpression(new ArrayList<>(List.of("special", "number", "letter", "uppercase", "lowercase")), defaultID)
        ))));
    }
}
