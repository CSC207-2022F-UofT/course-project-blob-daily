package com.backend.entities.criteria;


import com.backend.entities.criteria.conditions.CriteriaExpression;


public interface verifiable {
    default boolean isValid(String input, Criteria criteria){
        for (CriteriaExpression expression : criteria.getExpressions()) {
            expression.setTarget(input);
            if (!expression.evaluate()) {
                return false;
            }
        }
        return true;
    }
}
