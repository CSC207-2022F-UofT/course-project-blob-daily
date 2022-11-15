package com.questpets.entities.IDs;

import com.questpets.entities.criteria.Criteria;
import com.questpets.entities.criteria.conditions.ContainsAtleastTypeExpression;
import com.questpets.entities.criteria.conditions.ContainsOnlyTypeExpression;
import com.questpets.entities.criteria.conditions.SizeExpression;

import java.util.ArrayList;
import java.util.List;

public class SessionID extends ID {
    public SessionID(String defaultID) {
        super(defaultID, new Criteria(new ArrayList<>(List.of(
                new SizeExpression(16, defaultID),
                new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number", "letter")), defaultID),
                new ContainsAtleastTypeExpression(new ArrayList<>(List.of("number", "letter", "uppercase", "lowercase")), defaultID)
        ))));
    }
}
