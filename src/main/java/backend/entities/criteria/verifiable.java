package backend.entities.criteria;

public interface verifiable {
    default boolean isValid(String input, Criteria criteria){
        for (CriteriaExpression expression : criteria.expressions()) {
            if (!expression.evaluate(null, null)) {
                System.out.printf("The given ID %s is not valid by the given criteria: %s%n", input, expression.getExpression());
                return false;
            }
        }
        return true;
    }
}
