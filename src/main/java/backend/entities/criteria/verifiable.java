package backend.entities.criteria;

import backend.entities.criteria.conditions.CriteriaExpression;

public interface verifiable {
    default boolean isValid(String input, Criteria criteria){
        for (CriteriaExpression expression : criteria.getExpressions()) {
            expression.setTarget(input);
            if (!expression.evaluate()) {
                System.out.printf("The given ID %s is not valid by the given criteria, %s%n", input, expression.getExpression());
                return false;
            }
        }
        return true;
    }
}
