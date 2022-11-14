package backend.entities.criteria.conditions;

import java.util.ArrayList;
import java.util.HashMap;

public class ContainsAtleastTypeExpression extends CriteriaExpression{
    // Constructor
    public ContainsAtleastTypeExpression(ArrayList<String> values, String target) {
        super(values, target);
    }

    // Evaluate the target based on values
    public boolean evaluate() {
        boolean result = true;
        HashMap<String, String> types = super.getLegend();

        for (String type : super.getTypeList()) {
            result = result && this.singleContainsAtleastType(types.get(type));
        }

        return result;
    }

    private boolean singleContainsAtleastType(String type) {
        for (char c : super.getTarget().toCharArray()) {
            if (type.indexOf(c) != -1) return true;
        }
        return false;
    }
}
