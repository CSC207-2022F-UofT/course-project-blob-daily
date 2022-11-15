package com.questpets.entities.criteria.conditions;

import java.util.ArrayList;
import java.util.HashMap;

public class ContainsAtleastTypeExpression extends CriteriaExpression {
    // Constructor
    public ContainsAtleastTypeExpression(ArrayList<String> values, String target) {
        super(values, target);
    }

    // Evaluate the target based on values
    public boolean evaluate() {
        boolean result = true;

        for (String type : super.getTypeList()) {
            result = result && this.singleContainsAtleastType(type);
        }

        return result;
    }

    private boolean singleContainsAtleastType(String type) {
        HashMap<String, String> types = super.getLegend();

        for (char c : super.getTarget().toCharArray()) {
            if (types.get(type).indexOf(c) != -1) return true;
        }
        String errorMessage = String.format("The string '%s' doesn't contain any characters of type %s", super.getTarget(), type);
        super.logError(errorMessage);
        return false;
    }
}
