package com.backend.entities.criteria.conditions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Representation for a Contains Only Type based CriteriaExpression
 */
public class ContainsOnlyTypeExpression extends CriteriaExpression{
    // Constructor
    public ContainsOnlyTypeExpression(ArrayList<String> values, String target) {
        super(values, target);
    }

    /**
     * Evaluate whether the current target satisfies this Contains Only Type Criteria Expression with the known TypeList (values)
     * @return whether the current target satisfies this Contains Only Type Criteria Expression
     */
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
