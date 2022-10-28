package backend.entites.criteria;

public interface verifiable {
    default boolean isValid(String input, Criteria criteria) throws Exception {
        for (CriteriaExpression expression : criteria.expressions()) {
            if (!expression.evaluate(null, null)) throw new Exception(String.format("The given ID %s is not valid by the given criteria: %s", input, expression.getExpression()));
        }
        return true;
    }
}
