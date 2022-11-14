package backend.entities.IDs;

import backend.entities.criteria.Criteria;
import backend.entities.criteria.conditions.ContainsOnlyTypeExpression;
import backend.entities.criteria.conditions.SizeExpression;

import java.util.ArrayList;
import java.util.List;

public class ItemID extends ID{
    public ItemID(String defaultID) {
        super(defaultID, new Criteria(new ArrayList<>(List.of(
                new SizeExpression(10, defaultID),
                new ContainsOnlyTypeExpression(new ArrayList<>(List.of("number")), defaultID)
        ))));
    }
}
