package com.backend.entities.IDs;

import com.backend.entities.criteria.Criteria;
import com.backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
import com.backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import com.backend.entities.criteria.conditions.SizeExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a AccountID Entity (ID, validity, generation, etc)
 */
public class AccountID extends ID {

    public static final Criteria criteria = new Criteria(new ArrayList<>(List.of(
            new SizeExpression(20, null),
            new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter", "special")), null),
            new ContainsAtleastTypeExpression(new ArrayList<>(List.of("special", "number", "letter", "uppercase", "lowercase")), null)
    )));

    public AccountID(String defaultID) {
        super(defaultID, criteria);
    }
}
