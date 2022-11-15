package backend.entities.criteria;

import backend.entities.criteria.conditions.CriteriaExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Criteria {
    // Instance Variables
    private final List<CriteriaExpression> expressions;

    // Constructor
    public Criteria(List<CriteriaExpression> expressions) {
        this.expressions = expressions;
    }

    // Getter
    public List<CriteriaExpression> getExpressions() {
        return this.expressions;
    }

    public HashMap<String, String> getLegend() {
        return this.expressions.get(0).getLegend();
    }

    // Criteria Methods
    @Override
    public String toString() {
        List<String> expressionStrings = new ArrayList<>();

        for (CriteriaExpression c : this.expressions) {
            expressionStrings.add(c.getExpression());
        }

        return expressionStrings.toString();
    }
}
