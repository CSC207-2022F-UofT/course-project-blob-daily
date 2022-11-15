package com.questpets.entities.criteria;


import com.questpets.entities.criteria.conditions.CriteriaExpression;

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
