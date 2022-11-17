package backend.entities.IDs;

import backend.entities.criteria.Criteria;
import backend.entities.criteria.CriteriaExpression;

import java.util.ArrayList;
import java.util.List;

public class ItemID extends ID{
    public ItemID(String defaultID) {
        super(defaultID, new Criteria(new ArrayList<CriteriaExpression>(List.of(
                new CriteriaExpression("10", "size", defaultID),
                new CriteriaExpression("number", "contains type", defaultID)
        ))));
    }
}