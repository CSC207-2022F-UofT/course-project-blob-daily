package backend.entities.criteria;

import java.util.ArrayList;
import java.util.List;

public record Criteria(List<CriteriaExpression> expressions) {
    @Override
    public String toString() {
        List<String> expressionStrings = new ArrayList<>();

        for (CriteriaExpression c : this.expressions) {
            expressionStrings.add(c.getExpression());
        }

        return expressionStrings.toString();
    }
}
