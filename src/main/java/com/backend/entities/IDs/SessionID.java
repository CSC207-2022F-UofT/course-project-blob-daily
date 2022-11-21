package com.backend.entities.IDs;

import com.backend.entities.criteria.Criteria;
import com.backend.entities.criteria.CriteriaExpression;

import java.util.ArrayList;
import java.util.List;

public class SessionID extends com.backend.entities.IDs.ID {
    public SessionID(String defaultID) {
        super(defaultID, new Criteria(new ArrayList<CriteriaExpression>(List.of(
                new CriteriaExpression("16", "size", defaultID),
                new CriteriaExpression(new ArrayList<String>(List.of("number", "letter")), defaultID),
                new CriteriaExpression("number", "contains type", defaultID),
                new CriteriaExpression("letter", "contains type", defaultID),
                new CriteriaExpression("uppercase", "contains type", defaultID),
                new CriteriaExpression("lowercase", "contains type", defaultID)
        ))));
    }
}
