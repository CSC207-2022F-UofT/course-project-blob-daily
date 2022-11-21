package com.backend.entities.criteria.conditions;

/**
 * Representation for a Size Range based CriteriaExpression
 */
public class SizeRangeExpression extends CriteriaExpression{
    // Instance Variables
    private final int min;
    private final int max;


    // Constructor
    public SizeRangeExpression(int min, int max, String target) {
        super(min + " - " + max, "size of range", target);

        this.min = min;
        this.max = max;
    }

    /**
     * Evaluate whether the current target satisfies this Size Range Criteria Expression with the known instance variables (min, max)
     * @return whether the current target satisfies this Size Range Criteria Expression
     */
    public boolean evaluate() {
        if (this.min <= super.getTarget().length() && super.getTarget().length() <= this.max) {
            return true;
        }
        String errorMessage = String.format("The string '%s' (length: %s) must be between %s and %s characters long", super.getTarget(), super.getTarget().length(), this.min, this.max);
        super.logError(errorMessage);
        return false;
    }

    /**
     * Get the minimum size requirement of this Size Criteria Expression
     * @return The current min variable
     */
    @SuppressWarnings("unused")
    public int getMin() {
        return this.min;
    }

    /**
     * Get the maximum size requirement of this Size Criteria Expression
     * @return The current max variable
     */
    public int getMax() {
        return this.max;
    }
}
