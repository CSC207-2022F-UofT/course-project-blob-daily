package backend.entities.criteria.conditions;

import backend.error.exceptions.ConditionException;
import backend.error.handlers.LogHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class ContainsOnlyTypeExpression extends CriteriaExpression{
    // Constructor
    public ContainsOnlyTypeExpression(ArrayList<String> values, String target) {
        super(values, target);
    }

    // Evaluate the target based on values
    public boolean evaluate() {
        HashMap<String, String> types = super.getLegend();
        StringBuilder typeString = new StringBuilder();

        for (String type : super.getTypeList()) {
            typeString.append(types.get(type));
        }

        for(char c : super.getTarget().toCharArray()) {
            if (typeString.toString().indexOf(c) == -1) {
                String errorMessage = String.format("The string '%s' contains the illegal characters '%s' only %s types are allowed", super.getTarget(), c, super.getTypeList());
                super.logError(errorMessage);
                return false;
            }
        }

        return true;
    }
}
