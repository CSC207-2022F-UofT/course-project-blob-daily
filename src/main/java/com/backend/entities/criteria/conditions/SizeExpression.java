package com.backend.entities.criteria.conditions;

/**
 * Representation for a Size based CriteriaExpression
 */
public class SizeExpression extends CriteriaExpression{
    // Instance Variables
    private final int value;

    // Constructor
    public SizeExpression(int value, String target) {
        super(String.valueOf(value), "size", target);

        this.value = value;
    }

    /**
     * Evaluate whether the current target satisfies this Size Criteria Expression with the known instance variable (value)
     * @return whether the current target satisfies this Size Criteria Expression
     */
    public boolean evaluate() {
        if (super.getTarget().length() != this.value) {
            String errorMessage = String.format("The string '%s' (length: %s) must be exactly %s characters long", super.getTarget(), super.getTarget().length(), this.value);
            super.logError(errorMessage);
            return false;
        }
        return true;
    }
}
