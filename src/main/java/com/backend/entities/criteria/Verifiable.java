package com.backend.entities.criteria;

import com.backend.entities.criteria.conditions.CriteriaExpression;

/**
 * Interface to support verifying a string for a given criteria
 */
public interface Verifiable {
    /**
     * Check if the given input is valid based on the given criteria
     * @param input of type String, input to be checked for validity
     * @param criteria of type Criteria, criteria to check the given input against
     * @return Whether the given input is valid based on the given criteria
     */
    default boolean isValid(String input, Criteria criteria){
        for (CriteriaExpression expression : criteria.getExpressions()) {
            expression.setTarget(input);
            if (input == null){
                return false;
            }
            if (!expression.evaluate()) {
                return false;
            }
        }
        return true;
    }
}
