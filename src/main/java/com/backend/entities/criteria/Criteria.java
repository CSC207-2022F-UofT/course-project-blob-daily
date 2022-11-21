package com.backend.entities.criteria;

import com.backend.entities.criteria.conditions.CriteriaExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Representation of a Criteria Entity (CriteriaExpressions, Legend, etc)
 */
public class Criteria {
    // Instance Variables
    private final List<CriteriaExpression> expressions;

    // Constructor
    public Criteria(List<CriteriaExpression> expressions) {
        this.expressions = expressions;
    }

    // Getter

    /**
     * Get the current expression list for this criteria instance
     * @return the expressions variable
     */
    public List<CriteriaExpression> getExpressions() {
        return this.expressions;
    }

    /**
     * Get the current legend for this criteria instance
     * @return the legend variable
     */
    public HashMap<String, String> getLegend() {
        return this.expressions.get(0).getLegend();
    }

    // Criteria Methods

    /**
     * Retrieve the current string representation of Criteria
     * @return the current string representation of the Criteria
     */
    @Override
    public String toString() {
        List<String> expressionStrings = new ArrayList<>();

        for (CriteriaExpression c : this.expressions) {
            expressionStrings.add(c.getExpression());
        }

        return expressionStrings.toString();
    }
}
