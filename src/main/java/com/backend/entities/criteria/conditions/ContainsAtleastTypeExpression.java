package com.backend.entities.criteria.conditions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Representation for a Contains At Least Type based CriteriaExpression
 */
public class ContainsAtleastTypeExpression extends CriteriaExpression {
    // Constructor
    public ContainsAtleastTypeExpression(ArrayList<String> values, String target) {
        super(values, target);
    }

    /**
     * Evaluate whether the current target satisfies this Contains At Least Type Criteria Expression with the known TypeList (values)
     * @return whether the current target satisfies this Contains At Least Type Criteria Expression
     */
    public boolean evaluate() {
        boolean result = true;

        for (String type : super.getTypeList()) {
            result = result && this.singleContainsAtleastType(type);
        }

        return result;
    }

    /**
     * Helper Function : whether the current target satisfies this Contains At Least Type Criteria Expression with the given type
     * @param type of type String, the type of character to test the current target
     * @return whether the current target satisfies this Contains At Least Type Criteria Expression with the given type
     */
    private boolean singleContainsAtleastType(String type) {
        HashMap<String, String> types = super.getLegend();

        if (super.getTarget().length() == 64 && type.equals("special")) {
            // Hashed Passwords
            return true;
        }

        for (char c : super.getTarget().toCharArray()) {
            if (types.get(type).indexOf(c) != -1) return true;
        }
        String errorMessage = String.format("The string '%s' doesn't contain any characters of type %s", super.getTarget(), type);
        super.logError(errorMessage);
        return false;
    }
}
