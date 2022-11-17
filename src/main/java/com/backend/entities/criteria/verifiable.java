package com.backend.entities.criteria;


import com.backend.entities.criteria.conditions.CriteriaExpression;

import backend.entities.criteria.conditions.CriteriaExpression;
import backend.error.handlers.LogHandler;

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
