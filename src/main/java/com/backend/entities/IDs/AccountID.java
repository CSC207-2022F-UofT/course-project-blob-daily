package com.backend.entities.IDs;

import com.backend.entities.criteria.Criteria;
import com.backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
import com.backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import com.backend.entities.criteria.conditions.SizeExpression;

import java.util.ArrayList;
import java.util.List;

public class AccountID extends ID {
    public AccountID(String defaultID) {
        super(defaultID, new Criteria(new ArrayList<>(List.of(
                new SizeExpression(20, defaultID),
                new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), defaultID),
                new ContainsAtleastTypeExpression(new ArrayList<>(List.of("special", "number", "letter", "uppercase", "lowercase")), defaultID)
        ))));
    }
}
