package com.questpets.entities.criteria.conditions;

public class SizeRangeExpression extends CriteriaExpression{

    private final int min;
    private final int max;


    public SizeRangeExpression(int min, int max, String target) {
        super(String.valueOf(min) + " - " + String.valueOf(max), "size of range", target);

        this.min = min;
        this.max = max;
    }

    public boolean evaluate() {
        if (this.min <= super.getTarget().length() && super.getTarget().length() <= this.max) {
            return true;
        }
        String errorMessage = String.format("The string '%s' (length: %s) must be between %s and %s characters long", super.getTarget(), super.getTarget().length(), this.min, this.max);
        super.logError(errorMessage);
        return false;
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }
}
