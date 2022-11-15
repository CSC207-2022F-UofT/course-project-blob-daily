package backend.entities.criteria.conditions;

import backend.error.exceptions.ConditionException;
import backend.error.handlers.LogHandler;

public class SizeExpression extends CriteriaExpression{

    private final int value;


    public SizeExpression(int value, String target) {
        super(String.valueOf(value), "size", target);

        this.value = value;
    }

    public boolean evaluate() {
        if (super.getTarget().length() != this.value) {
            String errorMessage = String.format("The string '%s' (length: %s) must be exactly %s characters long", super.getTarget(), super.getTarget().length(), this.value);
            super.logError(errorMessage);
            return false;
        }
        return true;
    }
}
